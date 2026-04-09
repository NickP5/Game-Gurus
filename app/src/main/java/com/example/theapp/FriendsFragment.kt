package com.example.theapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theapp.databinding.FragmentFriendsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.getField

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

        val recyclerView = binding.friendsRecyclerView

        val db = Firebase.firestore
        val docRef = db.collection("users")

        //Getting name of loggedInUser
        docRef.document("$loggedInUser").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.get("friends") != null){

                    val listOfFriendIDs = documentSnapshot.getField("friends") as List<String>?

                    var friends = listOf<Friend>()
                    //They have at leat one friend, get necessary data to make a friend object using it
                    val friendID = listOfFriendIDs?.get(0)
                    docRef.document("$friendID").get()
                        .addOnSuccessListener { documentSnapshot ->
                            val friendName = documentSnapshot.getString("username")
                            val friendUsername = documentSnapshot.getString("username")
                            val friendUserID = documentSnapshot.getString("userID")?.toInt()

                            friends = listOf(
                                Friend(0, "$friendUsername")
                            )

                            val friendsSection = listSections(friends)

                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.adapter = FriendsAdapter(friendsSection)

                            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                                requireActivity().finish()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(FriendsTAG, "Error getting documents: ", exception)
                        }


                    Log.d(FriendsTAG, "Got friends: $friends")


                    val friendsSection = listSections(friends)

                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = FriendsAdapter(friendsSection)

                    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                        requireActivity().finish()
                    }
            }



            }

            }

//        val friends = listOf(
//            Friend(0, "Achane", "totallyNotFRENCH"),
//            Friend(0, "Bob", "bobby"),
//            Friend(0, "Charles", "chuck_cause_why_not"),
//            Friend(0, "Richard", "dick_also_cause_why_not"),
//            Friend(0, "Joodles", "TheBigFrenchman"),
//            Friend(0, "Jonathan", "jjj"),
//            Friend(0, "Justin", "Ajustinmygrip"),
//            Friend(0, "Pooh Shiesty", "ThePoohShiesty"),
//            Friend(0, "Rick", "picklerick"),
//            Friend(0, "Bob", "TheOtherBob"),
//            Friend(0, "Thomas", "PimpinAintEasy"),
//            Friend(0, "Louis Yu", "WorldsNumber1CrossFitFan")
//        )



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun listSections(friends: List<Friend>): List<ListItem> {
        val sorted = friends.sortedBy { it.username.lowercase() }
        val result = mutableListOf<ListItem>()
        var currentLetter: Char? = null
        for (friend in sorted) {
            var firstLetter = friend.username.first().uppercaseChar()
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