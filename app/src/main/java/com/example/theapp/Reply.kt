package com.example.theapp

import android.os.Parcel
import android.os.Parcelable

class Reply(
    val replyID: Int,
    val replyAnswer: String,
    val replyComment: String,
    val replyPoster: String,
    val replyPosterID: Int,
    var replyGrade: Int = 0
    ) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(replyID)
        parcel.writeString(replyAnswer)
        parcel.writeString(replyComment)
        parcel.writeString(replyPoster)
        parcel.writeInt(replyPosterID)
        parcel.writeInt(replyGrade)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Reply> {
        override fun createFromParcel(parcel: Parcel) = Reply(parcel)
        override fun newArray(size: Int) = arrayOfNulls<Reply?>(size)
    }

    fun gradeReply(postAnswer: String?) {
        val lowerReply = replyAnswer.lowercase()
        val lowerAnswer = postAnswer?.lowercase()

        replyGrade = if (lowerReply == lowerAnswer) {
            1
        } else {
            -1
        }
    }
}