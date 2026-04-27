package com.example.theapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postID = arguments?.getInt("postID") ?: 0
        val postClue = arguments?.getString("postClue")
        val postRating = arguments?.getString("postRating")
        val postOP = arguments?.getString("postOP")
        val postAnswer = arguments?.getString("postAnswer")


        // i am now realizing that this code is only needed for the original testpostlist posts
        // because their ratings are still stored as "x/5" but new posts automatically store
        // as the number in string format
        val ratingStringSplit = postRating?.split("/")
        val ratingNum = ratingStringSplit?.elementAt(0)

        // this is still needed though i think
        if (ratingNum != null) {
            binding.originalPostRating.rating = ratingNum.toFloat()
        }

        binding.originalPostClue.text = postClue
        binding.originalPostOP.text = postOP

        binding.replyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.replyRecyclerView.visibility = View.GONE

        val db = Firebase.firestore
        db.collection("users").document("$loggedInUser").get()
            .addOnSuccessListener { documentSnapshot ->
                val currentUserName = documentSnapshot.getString("username")

                //checking if the post is the logged in user's post
                if (currentUserName == postOP) {
                    // if it is, hide reply button and show all replies
                    binding.addReply.visibility = View.GONE
                    unlockAndLoadReplies(postID)
                } else {
                    // not user's own post, we now check if they already replied
                    db.collection("replies")
                        .whereEqualTo("postID", postID)
                        .whereEqualTo("userID", loggedInUser)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            if (!querySnapshot.isEmpty) {
                                // user has already replied
                                unlockAndLoadReplies(postID)
                                binding.addReply.visibility = View.GONE
                            } else {
                                // user has not replied
                                binding.addReply.visibility = View.VISIBLE
                            }
                        }
                }
            }

        parentFragmentManager.setFragmentResultListener("reply_key", viewLifecycleOwner) { _, bundle ->
            unlockAndLoadReplies(postID)
            binding.addReply.visibility = View.GONE
        }

//        val replyAdapter = ReplyAdapter(mutableListOf())
//
//        binding.replyRecyclerView.adapter = replyAdapter
//
//        parentFragmentManager.setFragmentResultListener(
//            "reply_key",
//            viewLifecycleOwner
//        ) { _, bundle ->
//
//            val newReply =
//                bundle.getParcelable("reply", Reply::class.java)
//
//            newReply?.let {
//                replyAdapter.addReply(it)
//                binding.replyRecyclerView.scrollToPosition(replyAdapter.itemCount - 1)
//            }


        binding.addReply.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("postID", postID)
                putString("postAnswer", postAnswer)
            }

            AddReplyFragment().apply {
                arguments = bundle
            }.show(parentFragmentManager, "AddReply")
        }
    }

                // quick dumb implementation of a friend button because that's a task i still need to do,
                // but since we haven't merged everything yet adding functionality is not possible. -Hayden
//        val requestSent = getString(R.string.request_sent)
//        binding.addFriend.setOnClickListener {
//            val db = Firebase.firestore
//            val usersRef = db.collection("users")
//            var loggedInUserName = ""
//
//            usersRef.document("$loggedInUser").get()
//                .addOnSuccessListener { documentSnapshot ->
//                    loggedInUserName = documentSnapshot.getString("username").toString()
//
//                    //Checking if the loggedinUser is not looking at their post so they can't friend themselves
//                    //If they aren't they add the postOP's userID to their friends array
//                    if (loggedInUserName != postOP){
//
//                    //Getting the postOP's userID
//                        usersRef
//                            .whereEqualTo("username", postOP)
//                            .get()
//                            .addOnSuccessListener { querySnapshot ->
//                               val postOPID = querySnapshot.documents[0].getLong("userID")?.toInt()
//                                if (postOPID != null) {
//                                    //Adding the postOP's userID to the loggedinUser's friends array
//                                    usersRef
//                                        .document("$loggedInUser")
//                                        .update("friends", FieldValue.arrayUnion(postOPID))
//                                        .addOnSuccessListener {
//                                            binding.addFriend.setText(requestSent)
//                                        }
//                                }
//                            }
//                    } else {
//                        binding.addFriend.setText(R.string.cant_friend_yourself)
//                    }
//                }
//            binding.addFriend.setText(requestSent)
//        }

        private fun unlockAndLoadReplies(postID: Int) {
            val db = Firebase.firestore
            db.collection("replies")
                .whereEqualTo("postID", postID)
                .get()
                .addOnSuccessListener { documents ->
                    val replyList = mutableListOf<Reply>()
                    for (doc in documents) {
                        val reply = doc.toObject(Reply::class.java)
                        replyList.add(reply)
                    }

                    binding.replyRecyclerView.visibility = View.VISIBLE

                    binding.replyRecyclerView.layoutManager = LinearLayoutManager(context)
                    binding.replyRecyclerView.adapter = ReplyAdapter(replyList)
                }
                .addOnFailureListener { e ->
                    Log.e("PostFragment", "Error getting replies", e)
                }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}