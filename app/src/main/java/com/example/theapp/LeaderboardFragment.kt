package com.example.theapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentLeaderboardBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

private const val LeaderTAG = "Leaderboard"

class LeaderboardFragment : Fragment() {
    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // this v will become the loggedInUser

        val userName = loggedInUser

        val db = Firebase.firestore
        val docRef = db.collection("users")

        //Getting name of loggInUser
        docRef.document("$loggedInUser").get()
            .addOnSuccessListener { documentSnapshot ->
                val nameString = documentSnapshot.getString("username")
                Log.d(TAG, "Got nameString: $nameString")
                docRef.orderBy("points", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { documents ->
                        val leaderboardData = mutableListOf<List<Any>>()
                        var rank = 1

                        for (document in documents) {
                            val points = document.getLong("points") ?: 0L
                            val name = document.getString("username") ?: "Anonymous"
                            val firstLetter = name.take(1)
                            Log.d(TAG, "DocumentSnapshot data: $firstLetter, $points, $name")

                            //Formatting to same structure as below
                            leaderboardData.add(listOf(firstLetter, rank, name, points.toInt()))
                            rank++
                        }

                        Log.d(TAG, "Real nameString: $nameString")
                        val adapter =
                            LeaderboardAdapter(leaderboardData, requireContext(), nameString)

                        binding.leaderboardList.adapter = adapter
                    }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}