package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = requireActivity()
            .findNavController(R.id.nav_host_fragment_content_main)

        val notificationAdapter = NotificationsAdapter(mutableListOf(Notification("Add Friend", "Would you like to be my friend?", "David", false))) { notification ->
            val bundle = Bundle().apply {
                putString("destination", notification.subject)
                putString("senderUsername", notification.senderUsername)
                putString("subject", notification.subject)
                putString("content", notification.message)
                putBoolean("friendMessage", notification.addFriendMessage)
            }

            findNavController().navigate(R.id.notifications_to_notificationitem, bundle)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.notificationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = notificationAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}