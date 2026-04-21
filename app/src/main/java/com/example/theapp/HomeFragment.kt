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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

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

        //Fetching posts from database and mapping to post class
        val db = Firebase.firestore
        val postRef = db.collection("posts")

        //Getting posts ordered by postID descending, i.e. most recent post at top
        postRef.orderBy("pID", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val postList = mutableListOf<Post>()

                for (document in querySnapshot) {
                    //mapping to post object
                    val post = document.toObject(Post::class.java)
                    postList.add(post)
                }

                //Putting adapter in successlistener so homepage only displays after all data is retrieved
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
        // get temporary post list, will change to pull from database later
//        val postList = TestPostList.getPostData()

//        val postAdapter = HomeAdapter(postList) { post ->
//            val bundle = Bundle().apply {
//                putString("destination", post.postClue)
//                putString("postClue", post.postClue)
//                putString("postRating", post.postRating)
//                putString("postOP", post.postOP)
//                putSerializable("postAnswer", post.postAnswer)
//            }
            binding.recyclerView.adapter = postAdapter
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        //recyclerView.adapter = postAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}