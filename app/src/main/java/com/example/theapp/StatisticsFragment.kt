package com.example.theapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.theapp.databinding.FragmentStatisticsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import java.util.Locale

class StatisticsFragment : DialogFragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use global loggedInUser
        val currentUserID = loggedInUser
        if (currentUserID != 0) {
            loadUserStatistics(currentUserID)
        }

        binding.closeStatsButton.setOnClickListener {
            dismiss()
        }
    }

    private fun loadUserStatistics(userID: Int) {
        //  get user's points and name
        db.collection("users").document("$userID").get()
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val username = userDoc.getString("username") ?: "Anonymous"
                    val points = userDoc.getLong("points") ?: 0L
                    
                    binding.usernameText.text = username
                    binding.totalPoints.text = points.toString()

                    //  getting their rank and the gap to next
                    calculateRankAndGap(userID)

                    // get their win stats and streak
                    calculateWinStatsAndStreak(username)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Statistics", "Error loading user data", e)
            }
    }

    private fun calculateRankAndGap(userID: Int) {
        db.collection("users")
            .orderBy("points", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                var rank = 1
                var previousPoints: Long? = null

                for (doc in documents) {
                    val currentPoints = doc.getLong("points") ?: 0L
                    val docUserID = doc.getLong("userID")?.toInt() ?: 0

                    if (docUserID == userID) {
                        binding.rank.text = "#$rank"
                        
                        if (previousPoints != null) {
                            val gap = previousPoints - currentPoints + 1
                            binding.pointsTo.text = gap.toString()
                        } else {
                            binding.pointsTo.text = "0" // user is already rank 1
                        }
                        break
                    }
                    previousPoints = currentPoints
                    rank++
                }
            }
    }

    private fun calculateWinStatsAndStreak(username: String) {
        // Fetch all replies and sort locally to avoid Firestore Index requirement
        db.collection("replies")
            .whereEqualTo("name", username)
            .get()
            .addOnSuccessListener { documents ->
                // Sort by replyID descending to get the most recent reply first
                val replies = documents.toObjects(Reply::class.java)
                    .sortedByDescending { it.replyID ?: 0 }
                
                if (replies.isEmpty()) {
                    binding.winPercent.text = "0.0%"
                    binding.recentStreak.text = "0"
                    return@addOnSuccessListener
                }

                val perfectCount = replies.count { it.replyGrade == 100 }
                val winPercent = (perfectCount.toDouble() / replies.size) * 100
                binding.winPercent.text = String.format(Locale.US, "%.1f%%", winPercent)

                // current streak is found by counting consecutive perfectguesses starting from the most recent
                var streak = 0
                for (reply in replies) {
                    if (reply.replyGrade == 100) {
                        streak++
                    } else {
                        break
                    }
                }
                binding.recentStreak.text = streak.toString()
            }
            .addOnFailureListener { e ->
                Log.e("Statistics", "Error calculating win stats", e)
            }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}