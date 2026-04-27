package com.example.theapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.databinding.FragmentUserLookupBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue

class UserLookupFragment : Fragment() {
    private var _binding: FragmentUserLookupBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNav: BottomNavigationView

    private val TAG = "UserLookupFragment"

    private val allUsers = mutableListOf<User>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLookupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNav = requireActivity().findViewById(R.id.bottomNavigationView)
        ViewCompat.setOnApplyWindowInsetsListener(requireActivity().findViewById(android.R.id.content)) { _, insets ->
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

            bottomNav.isVisible = !isKeyboardVisible

            insets
        }

        binding.searchEditText.requestFocus()
        binding.searchEditText.post {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
        }

//        val allUsers = mutableListOf(
//            User(0, "Jarvis", 0),
//            User(1, "Hulk", 1),
//            User(2, "Mark", 3),
//            User(3, "Joggins", 0),
//            User(4, "Liasas", 2),
//            User(5, "JumpingWilly", 1),
//            User(6, "Rad", 1),
//            User(7, "Poaster", 3),
//            User(8, "Batman", 3),
//            User(9, "IronMan", 0),
//            User(10, "CaptainAmerica", 2)
//        )

        val adapter = UserAdapter { userToAdd ->
            addFriend(userToAdd)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = adapter

        db.collection("users")
            .orderBy("points", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                allUsers.clear()
                for ((index, document) in querySnapshot.documents.withIndex()) {
                    val username = document.getString("username") ?: continue
                    if (document.getLong("userID")?.toInt() == loggedInUser) continue
                    val points = document.getLong("points")?.toInt() ?: 0
                    val userId = document.getLong("userID")?.toInt() ?: index
                    allUsers.add(User(userId, username, points))
                    Log.d(TAG, "Loaded user: $username with $points points")
                }
                val currentQuery = binding.searchEditText.text.toString()
                adapter.submitList(filterUsers(currentQuery))
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error fetching users", e)
            }
        // --- Search filter (works on the live Firestore-populated list) ---
        binding.searchEditText.addTextChangedListener { text ->
            adapter.submitList(filterUsers(text.toString()))
        }
    }

    private fun addFriend(userToAdd: User) {
        val loggedInUserDoc = db.collection("users").document("$loggedInUser")

        loggedInUserDoc.get()
            .addOnSuccessListener { snapshot ->
                @Suppress("UNCHECKED_CAST")
                val currentFriends = snapshot.get("friends") as? List<Long> ?: emptyList()

                if (currentFriends.contains(userToAdd.uid?.toLong())) {
                    Toast.makeText(
                        requireContext(),
                        "${userToAdd.username} is already your friend!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "${userToAdd.username} already in friends list, skipping")
                } else {
                    loggedInUserDoc.update("friends", FieldValue.arrayUnion(userToAdd.uid ?: return@addOnSuccessListener))
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "${userToAdd.username} added as a friend!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "Successfully added ${userToAdd.username} as friend")
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                "Failed to add friend. Try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(TAG, "Error adding friend", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error reading current user's friends list", e)
            }
    }
    private fun filterUsers(query: String): List<User> {
        return if (query.isEmpty()) {
            // Show full leaderboard when search is empty
            allUsers.toList()
        } else {
            allUsers.filter {
                it.username?.contains(query, ignoreCase = true) == true
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}