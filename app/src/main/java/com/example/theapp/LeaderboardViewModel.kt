package com.example.theapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LeaderboardViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>?>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardEntry>?> = _leaderboard
    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username

    fun loadLeaderboard(loggedInUser: Int) {
        val docRef = db.collection("users")


        docRef.orderBy("points", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val leaderboardData = mutableListOf<LeaderboardEntry>()
                var rank = 1

                for (document in documents) {
                    val points = document.getLong("points") ?: 0L
                    val name = document.getString("username") ?: "Anonymous"
                    val firstLetter = name.take(1)
                    val idOfUser = document.getLong("userID")?.toInt() ?: 0
                    Log.d(TAG, "DocumentSnapshot data: $firstLetter, $points, $name")

                    //Formatting to same structure as below
                    leaderboardData.add(LeaderboardEntry(firstLetter, rank, name, points.toInt(), idOfUser))
                    rank++
                }

                _leaderboard.value = leaderboardData
            }
    }
}