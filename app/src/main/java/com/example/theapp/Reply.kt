package com.example.theapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reply(
    val replyID: Int? = 0,
    val replyAnswer: String? = "",
    val replyComment: String? = "",
    val replyPoster: String? = "",
    val replyPosterID: Int? = 0,
    var replyGrade: Int? = 0
    ) :
    Parcelable {

    fun gradeReply(postAnswer: String?) {
        val lowerReply = replyAnswer?.lowercase()
        val lowerAnswer = postAnswer?.lowercase()

        replyGrade = if (lowerReply == lowerAnswer) {
            1
        } else {
            -1
        }
    }
}