package com.example.theapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theapp.databinding.FragmentReplyHistoryBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.getValue

class ReplyHistoryFragment : Fragment() {
    private var _binding: FragmentReplyHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReplyHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve user's replies from their ID
        // userHistoryID contains the ID needed to retrieve all of user's replies
        val userHistoryID = viewModel.historyID
        val db = Firebase.firestore

        db.collection("users").document("$userHistoryID").get()
            .addOnSuccessListener { documentSnapshot ->
                val username = documentSnapshot.getString("username")

                db.collection("replies")
                    .whereEqualTo("name", username)
                    .get()
                    .addOnSuccessListener { documents ->
                        val firestoreReplies = documents.toObjects(Reply::class.java)

                        val replyAdapter = ReplyAdapter(firestoreReplies.toMutableList())

                        binding.replyHistoryRecyclerView.layoutManager =
                            LinearLayoutManager(context)
                        binding.replyHistoryRecyclerView.adapter = replyAdapter

                        if (firestoreReplies.isEmpty()) {
                            Log.d("ReplyHistory", "No replies found for user: $username")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("ReplyHistory", "Error getting replies: ", exception)
                    }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}