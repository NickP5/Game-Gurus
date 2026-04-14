package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theapp.HomeAdapter
import com.example.theapp.databinding.FragmentHomeBinding
import com.google.api.Distribution

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get temporary post list, will change to pull from database later
        val postList = TestPostList.getPostData()

        val postAdapter = HomeAdapter(postList) { post ->
            val bundle = Bundle().apply {
                putString("destination", post.postClue)
                putString("postClue", post.postClue)
                putString("postRating", post.postRating)
                putString("postOP", post.postOP)
                putSerializable("postAnswer", post.postAnswer)
            }

            findNavController().navigate(R.id.home_to_post, bundle)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = postAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}