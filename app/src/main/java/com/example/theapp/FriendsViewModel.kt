package com.example.theapp

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FriendsViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _friends = MutableStateFlow<List<Friend>?>(null)
    val friends: StateFlow<List<Friend>?> = _friends

    fun loadFriends(loggedInUser: Int) {
        if (_friends.value != null) return

        db.collection("users")
            .document("$loggedInUser")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val ids = documentSnapshot.get("friends") as? List<Long>
                if (ids.isNullOrEmpty()) {
                    _friends.value = emptyList()
                    return@addOnSuccessListener
                }

                val friendsList = mutableListOf<Friend>()
                var fetchedCount = 0

                for (id in ids) {
                    db.collection("users").document("$id").get()
                        .addOnSuccessListener { friendDoc ->
                            if (friendDoc.exists()) {
                                val friend = Friend(
                                    friendDoc.getLong("userID")?.toInt() ?: 0,
                                    (friendDoc.get("pfp") as? Long)?.toInt(),
                                    friendDoc.getString("username")
                                )
                                friendsList.add(friend)
                            }

                            fetchedCount++
                            if (fetchedCount == ids.size) {
                                _friends.value = friendsList
                                    .sortedBy { it.username?.lowercase() }
                            }
                        }
                }
            }
    }

    fun observeFriends(loggedInUser: Int) {

        val userId = loggedInUser.toString()

        db.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, _ ->
                val ids = snapshot?.get("friends") as? List<Long> ?: emptyList()

                fetchFriends(ids)
            }
    }

    fun removeFriend(friend: Friend, loggedInUser: Int) {
        db.collection("users")
            .document("$loggedInUser")
            .update("friends", FieldValue.arrayRemove(friend.friendId?.toLong()))
            .addOnSuccessListener {
                _friends.value = _friends.value
                    ?.filter { it.friendId != friend.friendId }
            }
    }

    private fun fetchFriends(ids: List<Long>) {
        val temp = mutableListOf<Friend>()
        var count = 0

        if (ids.isEmpty()) {
            _friends.value = emptyList()
            return
        }

        for (id in ids) {
            db.collection("users").document(id.toString()).get()
                .addOnSuccessListener { doc ->
                    doc.takeIf { it.exists() }?.let {
                        temp.add(
                            Friend(
                                it.getLong("userID")?.toInt() ?: 0,
                                (it.get("pfp") as? Long)?.toInt(),
                                it.getString("username")
                            )
                        )
                    }
                    count++
                    if (count == ids.size) {
                        _friends.value = temp
                            .sortedBy { it.username?.lowercase() }
                    }
                }
        }
    }
}