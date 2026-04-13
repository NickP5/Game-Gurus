package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter(private val friends: List<Friend>) :
    RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.friend_name_text)
        val profileImage: ImageView = itemView.findViewById(R.id.friend_profile_pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friends_item, parent, false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val friend = friends[position]
        holder.usernameText.text = friend.username
        holder.profileImage.setImageResource(
            when (friend.profileIndex ?: 0) {
                0 -> R.drawable.ic_profile
                else -> R.drawable.ic_profile
            }
        )
    }

    override fun getItemCount(): Int = friends.size
}