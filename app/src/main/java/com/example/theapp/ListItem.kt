package com.example.theapp

sealed class ListItem {
    data class Header(val letter: Char) : ListItem()
    data class FriendItem(val friend: Friend) : ListItem()
}