package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.theapp.databinding.FragmentAddReplyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class AddReplyFragment() : BottomSheetDialogFragment() {

    private var _binding: FragmentAddReplyBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNav: BottomNavigationView

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
                val reply = Reply(0, answerText, commentText, "Temp User", loggedInUser)
                reply.gradeReply(postAnswer)
                parentFragmentManager.setFragmentResult(
                    "reply_key",
                    Bundle().apply {
                        putParcelable("reply", reply)
                    }
                )

                dismiss()
            } else {
                Snackbar.make(view, "Text field cannot be left blank", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.postButton).show()
            }
        }

        binding.closeReplyButton.setOnClickListener { view ->
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