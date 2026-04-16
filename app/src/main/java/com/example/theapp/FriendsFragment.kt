package com.example.theapp

import android.graphics.pdf.models.ListItem
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

                                    recyclerView.layoutManager = LinearLayoutManager(context)
                                    recyclerView.adapter = FriendsAdapter(friendsSection)

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


                    //val friendsSection = listSections(friends)

                    //recyclerView.layoutManager = LinearLayoutManager(context)
                    //recyclerView.adapter = FriendsAdapter(friendsSection)

                    //requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    //requireActivity().finish()
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

    fun listSections(friends: List<Friend>): List<Friend> {
        val sorted = friends.sortedBy { it.username!!.lowercase() }
        return sorted
    }
}
