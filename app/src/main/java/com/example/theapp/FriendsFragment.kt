package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theapp.databinding.FragmentFriendsBinding

private const val FriendsTAG = "Friends"

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

        val recyclerView = binding.outerRecyclerView

//        val db = Firebase.firestore
//        val docRef = db.collection("users")
//
//        //Getting name of loggedInUser
//        docRef.document("$loggedInUser").get()
//            .addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.get("friends") != null){
//
//                    val listOfFriendIDs = documentSnapshot.getField("friends") as List<String>?
//
//                    var friends = listOf<Friend>()
//                    //They have at leat one friend, get necessary data to make a friend object using it
//                    val friendID = listOfFriendIDs?.get(0)
//                    docRef.document("$friendID").get()
//                        .addOnSuccessListener { documentSnapshot ->
//                            val friendUsername = documentSnapshot.getString("username")
//                            val friendUserID = documentSnapshot.getString("userID")?.toInt()
//
//                            friends = listOf(
//                                Friend(friendUserID, 0, "$friendUsername")
//                            )
//
//                            val friendsSection = listSections(friends)
//
//                            recyclerView.layoutManager = LinearLayoutManager(context)
//                            recyclerView.adapter = OuterAdapter(friendsSection)
//
//                            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//                                requireActivity().finish()
//                            }
//                        }
//                        .addOnFailureListener { exception ->
//                            Log.d(FriendsTAG, "Error getting documents: ", exception)
//                        }
//
//
//                    Log.d(FriendsTAG, "Got friends: $friends")
//
//
//                    val friendsSection = listSections(friends)
//
//                    recyclerView.layoutManager = LinearLayoutManager(context)
//                    recyclerView.adapter = OuterAdapter(friendsSection)
//
//                    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//                        requireActivity().finish()
//                    }
//            }
//
//
//
//            }
//
//

        val friends = listOf(
            Friend(0, 0, "Achane"),
            Friend(1, 0, "Bob"),
            Friend(2, 0, "Charles"),
            Friend(3, 0, "Richard"),
            Friend(4, 0, "Joodles"),
            Friend(5, 0, "Jonathan"),
            Friend(6, 0, "Justin"),
            Friend(7, 0, "Pooh Shiesty"),
            Friend(8, 0, "Rick"),
            Friend(9, 0, "Bob"),
            Friend(10, 0, "Thomas"),
            Friend(11, 0, "Louis Yu")
        )

        val friendsSection = organizeFriends(friends)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = FriendsAdapter(friendsSection)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun organizeFriends(friends: List<Friend>): List<Friend> {
        val sorted = friends.sortedBy { it.username?.lowercase() }
        return sorted
    }
}