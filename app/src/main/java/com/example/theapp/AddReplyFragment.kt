package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.theapp.databinding.FragmentAddReplyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddReplyFragment() : BottomSheetDialogFragment() {

    private var _binding: FragmentAddReplyBinding? = null
    private val binding get() = _binding!!

    private val GEMINI_API_KEY = "AIzaSyC6N4Naja3jrpH03SL9ZbJM-XjVMdFrjLQ"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postAnswer = arguments?.getString("postAnswer")
        val postID = arguments?.getInt("postID") ?: 0

        binding.postButton.setOnClickListener {
            val answerText = binding.answerField.text.toString()
            val commentText = binding.commentField.text.toString()

            if (answerText.isNotBlank() && commentText.isNotBlank()) {

                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        binding.postButton.isEnabled = false
                        binding.postButton.text = "Grading..."

                        val db = Firebase.firestore
                        val repliesRef = db.collection("replies")
                        val usersRef = db.collection("users")

                        // Getting username of loggedinUser
                        val userSnapshot = usersRef.document(loggedInUser.toString()).get().await()
                        val username = userSnapshot.getString("username") ?: "Unknown User"

                        //Getting number of documents in replies collection for new replyID int
                        val dbCount = repliesRef.count().get(com.google.firebase.firestore.AggregateSource.SERVER).await()
                        val newReplyID = dbCount.count.toInt()


                        val reply = Reply(postID, newReplyID, answerText, commentText, username, loggedInUser)

                        reply.gradeReply(postAnswer, GEMINI_API_KEY)

                        val replyData = hashMapOf(
                            "postID" to reply.postID,
                            "replyID" to reply.replyID,
                            "answer" to reply.replyAnswer,
                            "comment" to reply.replyComment,
                            "name" to reply.replyPoster,
                            "userID" to reply.replyPosterID,
                            "grade" to reply.replyGrade
                        )

                        repliesRef.document(newReplyID.toString()).set(replyData).await()

                        parentFragmentManager.setFragmentResult(
                            "reply_key",
                            Bundle().apply {
                                putParcelable("reply", reply)
                            }
                        )

                        dismiss()
                    } catch (e: Exception) {
                        binding.postButton.isEnabled = true
                        binding.postButton.text = "Post"
                        Snackbar.make(
                            view,
                            "Grading failed. Check internet connection.",
                            Snackbar.LENGTH_LONG
                        ).show()

                    }
//                val reply = Reply(0, answerText, commentText, "Temp User", loggedInUser)
//                reply.gradeReply(postAnswer)
//                parentFragmentManager.setFragmentResult(
//                    "newReplyKey",
//                    bundleOf("reply" to reply)
                    //)

                    //findNavController().popBackStack()
                }
            } else {
                Snackbar.make(view, "Text field cannot be left blank", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.postButton).show()
            }
        }

        binding.closeReplyButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog as? com.google.android.material.bottomsheet.BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(it)

            behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = false
            behavior.peekHeight = 600
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}