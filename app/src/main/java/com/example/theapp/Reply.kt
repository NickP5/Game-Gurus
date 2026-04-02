package com.example.theapp

import android.os.Parcel
import android.os.Parcelable

class Reply(val replyAnswer: String, val replyComment: String, val replyPoster: String) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(replyAnswer)
        parcel.writeString(replyComment)
        parcel.writeString(replyPoster)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Reply> {
        override fun createFromParcel(parcel: Parcel) = Reply(parcel)
        override fun newArray(size: Int) = arrayOfNulls<Reply?>(size)
    }
}