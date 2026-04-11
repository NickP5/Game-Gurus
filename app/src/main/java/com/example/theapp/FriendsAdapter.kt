package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.VH>() {

    private val items = mutableListOf<ListItem.FriendItem>()

    fun submitList(newItems: List<ListItem.FriendItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.friend_name_text)
        val image: ImageView = view.findViewById(R.id.friend_profile_pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inner_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val friend = items[position].friend

        val profileImages = arrayOf(
            R.drawable.ic_profile
        )

        holder.username.text = friend?.username ?: ""
        holder.image.setImageResource(profileImages[friend?.profileIndex ?: 0])
    }
    override fun getItemCount(): Int = items.size
}