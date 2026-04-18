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
        uID = 0

        // Retrieve user's posts from their ID
        // userHistoryID contains the ID needed to check for post history
        val userHistoryID = viewModel.historyID

        // get posts of the user and send them through the home adapter
        val postList = TestPostList.getPostData()

        val postAdapter = HomeAdapter(postList) { post ->
            val bundle = Bundle().apply {
                putString("destination", post.postClue)
                putString("postClue", post.postClue)
                putString("postRating", post.postRating)
                putString("postOP", post.postOP)
                putSerializable("postAnswer", post.postAnswer)
            }

            findNavController().navigate(R.id.history_to_post, bundle)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.postHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = postAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}