package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter(private val items: List<ListItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_FRIEND = 1
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerText: TextView = view.findViewById(R.id.header_text)
    }

    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.friend_name_text)
        val usernameText: TextView = view.findViewById(R.id.friend_username_text)
        val profileImage: ImageView = view.findViewById(R.id.friend_profile_pic)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.Header -> TYPE_HEADER
            is ListItem.FriendItem -> TYPE_FRIEND
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.friends_item, parent, false)
                FriendViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ListItem.Header -> {
                (holder as HeaderViewHolder).headerText.text = item.letter.toString()
            }
            is ListItem.FriendItem -> {
                val friend = item.friend
                val vh = holder as FriendViewHolder
                val profileImages = arrayOf(
                    R.drawable.ic_profile
                )
                vh.nameText.text = friend.name
                vh.usernameText.text = friend.username
                vh.profileImage.setImageResource(profileImages[friend.profileIndex])
            }
        }
    }
    override fun getItemCount() = items.size
}