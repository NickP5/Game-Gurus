package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationsAdapter(private val notifications: MutableList<Notification>, private val onItemClick: (Notification) -> Unit) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    class NotificationsViewHolder(itemView: View, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(position)
                }
            }
        }
        val subject: TextView = itemView.findViewById(R.id.subjectLine)
        val content: TextView = itemView.findViewById(R.id.contentLine)
        val username: TextView = itemView.findViewById(R.id.senderUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationsViewHolder(view) {
            onItemClick(notifications[it])
        }
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notification = notifications[position]
        holder.subject.text = notification.subject
        holder.content.text = notification.message
        holder.username.text = notification.senderUsername
    }

    override fun getItemCount(): Int = notifications.size
}