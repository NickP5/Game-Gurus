package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class UserAdapter :
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
        val usernameText: TextView = view.findViewById(R.id.friend_name_text)
        val profileImage: ImageView = view.findViewById(R.id.friend_profile_pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.outer_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)

        holder.usernameText.text = user.username
        holder.profileImage.setImageResource(
            when (user.pfp ?: 0) {
                0 -> R.drawable.ic_profile
                else -> R.drawable.ic_profile
            }
        )
    }
}