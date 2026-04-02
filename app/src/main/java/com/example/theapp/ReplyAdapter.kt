package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReplyAdapter(private val replies: MutableList<Reply>) :
    RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    inner class ReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val replyUser: TextView = itemView.findViewById(R.id.replyPoster)
        val replyMessage: TextView = itemView.findViewById(R.id.replyAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reply_preview, parent, false)
        return ReplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val reply = replies[position]
        holder.replyUser.text = reply.replyPoster
        holder.replyMessage.text = reply.replyAnswer
    }

    override fun getItemCount(): Int = replies.size

    fun addReply(reply: Reply) {
        replies.add(reply)
        notifyItemInserted(replies.size - 1)
    }
}