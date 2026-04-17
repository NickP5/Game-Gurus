package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.databinding.FragmentPostBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

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
        val postAnswer = arguments?.getString("postAnswer")

        binding.originalPostClue.text = postClue
        binding.originalPostRating.text = postRating
        binding.originalPostOP.text = postOP

        val replyAdapter = ReplyAdapter(mutableListOf())

        binding.replyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.replyRecyclerView.adapter = replyAdapter

        parentFragmentManager.setFragmentResultListener(
            "reply_key",
            viewLifecycleOwner
        ) { _, bundle ->

            val newReply = bundle.getParcelable<Reply>("reply")

            newReply?.let {
                replyAdapter.addReply(it)
                binding.replyRecyclerView.scrollToPosition(replyAdapter.itemCount - 1)
            }
        }

        binding.addReply.setOnClickListener {
            val bundle = Bundle().apply {
                putString("postAnswer", postAnswer)
            }

            AddReplyFragment().apply {
                arguments = bundle
            }.show(parentFragmentManager, "AddReply")
        }

        // quick dumb implementation of a friend button because that's a task i still need to do,
        // but since we haven't merged everything yet adding functionality is not possible. -Hayden
        val requestSent = getString(R.string.request_sent)
        binding.addFriend.setOnClickListener {
            val db = Firebase.firestore
            val usersRef = db.collection("users")
            var loggedInUserName = ""

            usersRef.document("$loggedInUser").get()
                .addOnSuccessListener { documentSnapshot ->
                    loggedInUserName = documentSnapshot.getString("username").toString()

                    //Checking if the loggedinUser is not looking at their post so they can't friend themselves
                    //If they aren't they add the postOP's userID to their friends array
                    if (loggedInUserName != postOP){

                    //Getting the postOP's userID
                        usersRef
                            .whereEqualTo("username", postOP)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                               val postOPID = querySnapshot.documents[0].getLong("userID")?.toInt()
                                if (postOPID != null) {
                                    //Adding the postOP's userID to the loggedinUser's friends array
                                    usersRef
                                        .document("$loggedInUser")
                                        .update("friends", FieldValue.arrayUnion(postOPID))
                                        .addOnSuccessListener {
                                            binding.addFriend.setText(requestSent)
                                        }
                                }
                            }
                    } else {
                        binding.addFriend.setText(R.string.cant_friend_yourself)
                    }
                }
            binding.addFriend.setText(requestSent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}