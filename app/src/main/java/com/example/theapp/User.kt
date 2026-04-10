package com.example.theapp

data class User(
    val uid: Int? = 0,
    var username: String? = "",
    var pfp: Int? = 0,
    var postList: MutableList<Post>? = mutableListOf(),
    var replyList: MutableList<Reply>? = mutableListOf(),
    var friendList: MutableList<Friend>? = mutableListOf(),
    var notificationList: MutableList<Notification>? = mutableListOf(),
    var points: Long? = 0
) {

    fun addFriend(friendId: Int?, pfpIndex: Int?, friendUsername: String?) {
        friendList?.add(Friend(friendId, pfpIndex, friendUsername))
    }

    fun removeFriend(friendUserId: Int?) {
        friendList?.removeIf { it.friendId == friendUserId }
    }

    fun newNotification(username: String, subject: String, message: String, addFriend: Boolean) {
        var nid = 0
        if (notificationList?.isNotEmpty() == true) {
            nid = notificationList!![notificationList!!.size - 1].notificationId + 1
        }
        notificationList?.add(Notification(nid, subject, message, username, addFriend))
    }

    fun removeNotification(nid: Int) {
        notificationList?.removeIf { it.notificationId == nid }
    }
}