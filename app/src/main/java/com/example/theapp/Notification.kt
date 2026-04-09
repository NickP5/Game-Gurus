package com.example.theapp

class Notification {
    var read: Boolean = false
    val subject: String
    val message: String
    val senderUsername: String
    val addFriendMessage: Boolean

    constructor(subject: String, message: String, senderUsername: String, addFriendMessage: Boolean) {
        this.subject = subject
        this.message = message
        this.senderUsername = senderUsername
        this.addFriendMessage = addFriendMessage
    }

    fun readByUser() {
        read = true
    }
}