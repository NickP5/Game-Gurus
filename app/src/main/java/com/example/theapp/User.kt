package com.example.theapp

data class User(
    val uid: Int? = 0,
    var username: String? = "",
    var pfp: Int? = 0,
    var postList: MutableList<Post>? = mutableListOf(),
    var replyList: MutableList<Reply>? = mutableListOf(),
    var friendList: MutableList<Friend>? = mutableListOf(),
    var points: Long? = 0
) {

    fun addFriend(friendId: Int?, pfpIndex: Int?, friendUsername: String?) {
        friendList?.add(Friend(friendId, pfpIndex, friendUsername))
    }

    fun removeFriend(friendUserId: Int?) {
        friendList?.removeIf { it.friendId == friendUserId }
    }

    fun addPost() {

    }

    fun deletePost() {

    }

    fun addReply() {

    }

    fun deleteReply() {

    }

    fun changePfp() {

    }
}