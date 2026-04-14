package com.example.theapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val notificationId: Int? = 0,
    val subject: String? = "",
    val message: String? = "",
    val senderUsername: String? = "",
    val addFriendMessage: Boolean? = false,
    var read: Boolean? = false
) : Parcelable {

    fun readByUser() {
        read = true
    }
}