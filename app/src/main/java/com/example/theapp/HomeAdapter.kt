package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.Post
import com.example.theapp.R

class HomeAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_preview,
            parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val post = postList[position]

        holder.postClue.text = post.postClue
        holder.postRating.text = post.postRating
        holder.postOP.text = post.postOP
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postClue: TextView = itemView.findViewById(R.id.postClue)
        val postRating: TextView = itemView.findViewById(R.id.postRating)
        val postOP: TextView = itemView.findViewById(R.id.postOP)
    }
    }