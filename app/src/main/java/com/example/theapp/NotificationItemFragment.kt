package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentNotificationItemBinding

class NotificationItemFragment : Fragment() {
    private var _binding: FragmentNotificationItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val senderUsername = arguments?.getString("senderUsername")
        val subject = arguments?.getString("subject")
        val content = arguments?.getString("content")
        val friendMessage = arguments?.getBoolean("friendMessage")

        binding.senderUsername.text = senderUsername
        binding.subjectLine.text = subject
        binding.contentLine.text = content

        if (friendMessage == false) {
            binding.friendAccept.visibility = View.GONE
            binding.friendReject.visibility = View.GONE
        }

        binding.friendAccept.setOnClickListener { view ->
            // make each user friends of each other
            // remove the notification
        }

        binding.friendReject.setOnClickListener { view ->
            // remove the notification
            // do nothing
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}