package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theapp.databinding.FragmentFriendsBinding
import kotlinx.coroutines.launch

private const val FriendsTAG = "Friends"

class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()
    private val dataViewModel: FriendsLiveData by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.outerRecyclerView

        val adapter = FriendsAdapter(
            mutableListOf(),
            onViewHistory = { friend ->
                viewModel.historyID = friend.friendId ?: 0
                findNavController().navigate(R.id.friends_to_history)
            },
            onRemoveFriend = { friend ->
                dataViewModel.removeFriend(friend, loggedInUser)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataViewModel.friends.collect { friends ->
                    if (friends != null) {
                        adapter.updateData(friends)
                    }
                }
            }
        }
        dataViewModel.observeFriends(loggedInUser)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
