package com.example.theapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.Post
import com.example.theapp.R

class HomeAdapter(private val postList: List<Post>, private val onItemClick: (Post) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_preview,
            parent, false)
        return HomeViewHolder(view) {
            onItemClick(postList[it])
        }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val post = postList[position]

        holder.postClue.text = post.postClue
        holder.postOP.text = post.postOP

        // since rating is stored in the format "[rating]/5", we need to split the string
        // and then store our rating so we can set the number of stars that are displayed
        val ratingStringSplit = post.postRating?.split("/")
        val ratingNum = ratingStringSplit?.elementAt(0)

        if (ratingNum != null) {
            holder.postRatingStars.rating = ratingNum.toFloat()
        }

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class HomeViewHolder(itemView: View, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(position)
                }
            }
        }
        val postClue: TextView = itemView.findViewById(R.id.postClue)
        val postOP: TextView = itemView.findViewById(R.id.postOP)
        val postRatingStars: RatingBar = itemView.findViewById(R.id.postRatingStars)
        val guessButton: ImageView = itemView.findViewById(R.id.guessImage)
        val commentButton: ImageView = itemView.findViewById(R.id.commentImage)

    }
    }