package com.example.theapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theapp.databinding.FragmentFriendsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

private const val FriendsTAG = "Friends"

class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()

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

        val db = Firebase.firestore
        val docRef = db.collection("users")

        //Getting name of loggedInUser
        docRef.document("$loggedInUser").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.get("friends") != null) {

                    val listOfFriendIDs = documentSnapshot.get("friends") as? List<Long>

                    var friends = mutableListOf<Friend>()

                    var fetchedCount = 0
                    //They have at leat one friend, get necessary data to make a friend object using it
                    for (id in listOfFriendIDs!!) {
                        Log.d(FriendsTAG, "Got id: $id")
                        docRef.document("$id").get()
                            .addOnSuccessListener { friendDoc ->
                                if (friendDoc.exists()) {
                                       val friendPfp = (friendDoc.get("pfp") as? Long)?.toInt()
                                    val friendUsername = friendDoc.getString("username")
                                    val friendUserID = (friendDoc.getLong("userID") as? Long)?.toInt() ?: 0
                                    Log.d(FriendsTAG, "Got friend: $friendUsername, $friendPfp, $friendUserID")



                                    friends.add(
                                        Friend(friendUserID, friendPfp, "$friendUsername")
                                    )
                                }

                                fetchedCount++
                                if (fetchedCount == listOfFriendIDs.size) {

                                    Log.d(FriendsTAG, "Got friends: $friends")

                                    val friendsSection = listSections(friends)

                                    lateinit var adapter: FriendsAdapter

                                    adapter = FriendsAdapter(
                                        friendsSection,
                                        onViewHistory = { friend ->
                                            viewModel.historyID = friend.friendId ?: 0
                                            findNavController().navigate(R.id.friends_to_history)
                                        },
                                        onRemoveFriend = { friend ->
                                            // Remove as friend
                                            db.collection("users")
                                                .document("$loggedInUser")
                                                .update("friends", FieldValue.arrayRemove(friend.friendId?.toLong()))
                                                .addOnSuccessListener {
                                                    Log.d(FriendsTAG, "Friend removed")

                                                    // Update UI
                                                    val position = friendsSection.indexOfFirst { it.friendId == friend.friendId }
                                                    if (position != -1) {
                                                        friends.removeAll { it.friendId == friend.friendId }
                                                        friendsSection.removeAt(position)
                                                        adapter.notifyItemRemoved(position)
                                                    }
                                                }
                                                .addOnFailureListener { exception ->
                                                    Log.d(FriendsTAG, "Error removing friend", exception)
                                                }
                                        }
                                    )

                                    recyclerView.layoutManager = LinearLayoutManager(context)
                                    recyclerView.adapter = adapter

                                    requireActivity().onBackPressedDispatcher.addCallback(
                                        viewLifecycleOwner
                                    ) {
                                        requireActivity().finish()

                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(FriendsTAG, "Error getting documents: ", exception)
                            }
                    }
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun listSections(friends: List<Friend>): MutableList<Friend> {
        return friends
            .sortedBy { it.username!!.lowercase() }
            .toMutableList()
    }
}
