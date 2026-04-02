package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.databinding.FragmentFriendsBinding

class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

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

        val recyclerView = binding.friendsRecyclerView

        val friends = listOf(
            Friend(0, "Achane", "totallyNotFRENCH"),
            Friend(0, "Bob", "bobby"),
            Friend(0, "Charles", "chuck_cause_why_not"),
            Friend(0, "Richard", "dick_also_cause_why_not"),
            Friend(0, "Joodles", "TheBigFrenchman"),
            Friend(0, "Jonathan", "jjj"),
            Friend(0, "Justin", "Ajustinmygrip"),
            Friend(0, "Pooh Shiesty", "ThePoohShiesty"),
            Friend(0, "Rick", "picklerick"),
            Friend(0, "Bob", "TheOtherBob"),
            Friend(0, "Thomas", "PimpinAintEasy"),
            Friend(0, "Louis Yu", "WorldsNumber1CrossFitFan")
        )

        val friendsSection = listSections(friends)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = FriendsAdapter(friendsSection)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun listSections(friends: List<Friend>): List<ListItem> {
        val sorted = friends.sortedBy { it.name.lowercase() }
        val result = mutableListOf<ListItem>()
        var currentLetter: Char? = null
        for (friend in sorted) {
            var firstLetter = friend.name.first().uppercaseChar()
            if (!firstLetter.isLetter()) firstLetter = '#'

            if (firstLetter != currentLetter) {
                currentLetter = firstLetter
                result.add(ListItem.Header(currentLetter))
            }
            result.add(ListItem.FriendItem(friend))
        }
        return result
    }
}