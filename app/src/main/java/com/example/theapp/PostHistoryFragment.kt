package com.example.theapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theapp.databinding.FragmentPostHistoryBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.getValue

class PostHistoryFragment : Fragment() {
    private var _binding: FragmentPostHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve user's posts from their ID
        // userHistoryID contains the ID needed to check for post history
        val userHistoryID = viewModel.historyID

        binding.postHistoryRecyclerView.layoutManager = LinearLayoutManager(context)


        // get posts of the user and send them through the home adapter
        val db = Firebase.firestore
        val postsRef = db.collection("posts")
        val usersRef = db.collection("users")

        usersRef.document("$userHistoryID").get()
            .addOnSuccessListener { documentSnapshot ->
                val username = documentSnapshot.getString("username")
                postsRef
                    .whereEqualTo("name", username)
                    .get()
                    .addOnSuccessListener { documents ->
                        val firestorePosts = documents.toObjects(Post::class.java)
                        if (firestorePosts.isEmpty()) {
                            Log.d("PostHistory", "No posts found for user: $userHistoryID")
                        }

                        val postAdapter = HomeAdapter(firestorePosts) { post ->
                            val bundle = Bundle().apply {
                                putString("destination", post.postClue)
                                putString("postClue", post.postClue)
                                putString("postRating", post.postRating)
                                putString("postOP", post.postOP)
                                putString("postAnswer", post.postAnswer)
                            }
                            findNavController().navigate(R.id.history_to_post, bundle)
                        }
                        binding.postHistoryRecyclerView.adapter = postAdapter
                    }
                    .addOnFailureListener { exception ->
                        Log.e("PostHistory", "Error getting posts: ", exception)
                    }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}