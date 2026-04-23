package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val onAddFriend: (User) -> Unit):
    ListAdapter<User, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) =
                oldItem.uid == newItem.uid

            override fun areContentsTheSame(oldItem: User, newItem: User) =
                oldItem == newItem
        }
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameText: TextView = view.findViewById(R.id.userNameText)
        val profileImage: ImageView = view.findViewById(R.id.userProfilePic)
        val addFriendButton: View = view.findViewById(R.id.addUserAsFriend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)

        holder.usernameText.text = user.username
        holder.profileImage.setImageResource(
            when (user.pfp ?: 0) {
                0 -> R.drawable.black_mountain
                1 -> R.drawable.cropped_circle_image
                2 -> R.drawable.blue_mountain
                3 -> R.drawable.nyan_cat
                else -> R.drawable.cropped_circle_image
            }
        )
        holder.addFriendButton.setOnClickListener {
            onAddFriend(user)
        }
    }
}