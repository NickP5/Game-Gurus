package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.databinding.FragmentPostBinding

class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postClue = arguments?.getString("postClue")
        val postRating = arguments?.getString("postRating")
        val postOP = arguments?.getString("postOP")

        binding.originalPostClue.text = postClue
        binding.originalPostRating.text = postRating
        binding.originalPostOP.text = postOP

        // Add replies as a attribute of Post
        // Give the adapter the reply list
//        val replyAdapter = ReplyAdapter(Post.replies)

//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//
//        recyclerView.adapter = replyAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}