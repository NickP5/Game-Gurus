package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.databinding.FragmentNotificationsBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

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

        val recyclerView: RecyclerView = view.findViewById(R.id.notificationRecyclerView)

        val materialDivider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
            dividerColor = ContextCompat.getColor(requireContext(), R.color.divider_color)
            dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
            isLastItemDecorated = false
        }
        recyclerView.addItemDecoration(materialDivider)

        val notificationList = mutableListOf(Notification(0, "Add Friend", "Would you like to be my friend?", "David", true),
            Notification(1, "Bing-Bong", "Are you enjoying my fish", "SuspiciousSender11", false))

        val notificationAdapter = NotificationsAdapter(notificationList) { notification ->
            val bundle = Bundle().apply {
                putString("destination", notification.subject)
                putParcelable("notification", notification)
            }

            findNavController().navigate(R.id.notifications_to_notificationitem, bundle)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = notificationAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}