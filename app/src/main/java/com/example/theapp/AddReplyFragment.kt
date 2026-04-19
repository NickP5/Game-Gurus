package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.theapp.databinding.FragmentAddReplyBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddReplyFragment() : Fragment() {

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

        binding.postButton.setOnClickListener {
            val answerText = binding.answerField.text.toString()
            val commentText = binding.commentField.text.toString()

            if (answerText.isNotBlank() && commentText.isNotBlank()) {

                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        binding.postButton.isEnabled = false
                        binding.postButton.text = "Grading..."

                        val reply = Reply(0, answerText, commentText, "Temp User", loggedInUser)

                        reply.gradeReply(postAnswer, GEMINI_API_KEY)

                        parentFragmentManager.setFragmentResult(
                            "newReplyKey",
                            bundleOf("reply" to reply)
                        )
                        findNavController().popBackStack()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}