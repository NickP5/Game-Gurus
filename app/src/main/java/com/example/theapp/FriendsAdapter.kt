package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter(
    private val friends: MutableList<Friend>?,
    private val onViewHistory: (Friend) -> Unit,
    private val onRemoveFriend: (Friend) -> Unit
) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.friend_name_text)
        val profileImage: ImageView = itemView.findViewById(R.id.friend_profile_pic)
        val options: ImageView = itemView.findViewById(R.id.overflowOptions)
    }

    fun updateData(newList: List<Friend>) {
        friends?.clear()
        friends?.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friends_item, parent, false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val friend = friends!![position]
        holder.usernameText.text = friend.username
        holder.profileImage.setImageResource(
            when (friend.profileIndex ?: 0) {
                0 -> R.drawable.black_mountain
                1 -> R.drawable.cropped_circle_image
                2 -> R.drawable.blue_mountain
                3 -> R.drawable.nyan_cat
                else -> R.drawable.cropped_circle_image
            }
        )
        holder.options.setOnClickListener { view ->
            val popup = PopupMenu(view.context, holder.options)
            popup.inflate(R.menu.friend_options_menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_view_history -> {
                        onViewHistory(friend)
                        true
                    }
                    R.id.action_remove_friend -> {
                        onRemoveFriend(friend)
                        true
                    }
                    else -> true
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = friends!!.size
}