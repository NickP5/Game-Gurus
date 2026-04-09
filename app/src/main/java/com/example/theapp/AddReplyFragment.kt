package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.theapp.databinding.FragmentAddReplyBinding
import com.google.android.material.snackbar.Snackbar

class AddReplyFragment() : Fragment() {

    private var _binding: FragmentAddReplyBinding? = null
    private val binding get() = _binding!!

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

        binding.postButton.setOnClickListener {
            val answerText = binding.answerField.text.toString()
            val commentText = binding.commentField.text.toString()

            if (answerText.isNotBlank() && commentText.isNotBlank()) {
                val reply = Reply(answerText, commentText, "Temp User")
                parentFragmentManager.setFragmentResult(
                    "newReplyKey",
                    bundleOf("reply" to reply)
                )

                findNavController().popBackStack()
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