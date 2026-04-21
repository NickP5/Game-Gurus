package com.example.theapp

data class LeaderboardEntry(
    val firstLetter: String,
    val rank: Int,
    val username: String,
    val points: Int,
    val userID: Int
)