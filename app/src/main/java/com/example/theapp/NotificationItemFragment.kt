package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.theapp.databinding.FragmentNotificationItemBinding
import com.google.android.material.snackbar.Snackbar

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

        val notification = arguments?.getParcelable("notification", Notification::class.java)

        binding.senderUsername.text = notification?.senderUsername
        binding.subjectLine.text = notification?.subject
        binding.contentLine.text = notification?.message

        if (notification?.addFriendMessage == false) {
            binding.friendAccept.visibility = View.GONE
            binding.friendReject.visibility = View.GONE
        }

        binding.friendAccept.setOnClickListener { view ->
            // make each user friends of each other
            // remove the notification
            Snackbar.make(view, "Friend Request Accepted!", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.nav_host_fragment_content_main).show()
            findNavController().popBackStack()
        }

        binding.friendReject.setOnClickListener { view ->
            // remove the notification
            Snackbar.make(view, "Rejected Friend Request", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.nav_host_fragment_content_main).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}