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
                val adapter = LeaderboardAdapter(leaderboardData, requireContext(), userName.toString())
                binding.leaderboardList.adapter = adapter


            }

        val data = listOf(
            listOf("J", 1, "Jonathan", 20),
            listOf("j", 2, "Jon", 19),
            listOf("J", 3, "Joe", 18),
            listOf("j", 4, "Joesph", 17),
            listOf("J", 5, "John", 16),
            listOf("j", 6, "Jonny", 15),
            listOf("J", 7, "Johnny", 14),
            listOf("j", 8, "Johnn", 13),
            listOf("J", 9, "Johono", 12),
            listOf("j", 10, "Josh", 11),
            listOf("J", 11, "Joshua", 10),
            listOf("j", 12, "Joolo", 9),
            listOf("Jon", 13, "Julius", 8),
            listOf("j", 14, "Jobe", 7),
            listOf("J", 15, "Joni", 6),
            listOf("j", 16, "Jos", 5),
            listOf("J", 17, "Jordan", 4),
            listOf("j", 18, "Jory", 3),
            listOf("J", 19, "Jane", 2),
            listOf("j", 20, "Joodles", 1)
        )
        //val adapter = LeaderboardAdapter(data, requireContext(), userName)

        //binding.leaderboardList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}