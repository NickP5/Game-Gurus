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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class HomeAdapter(private val postList: List<Post>, private val onItemClick: (Post) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_preview,
            parent, false)
        return HomeViewHolder(view) {
//            onItemClick(postList[it])
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

        holder.addFriendImage.setOnClickListener {
            val db = Firebase.firestore
            val usersRef = db.collection("users")

            //get friendsArray
            usersRef.document("$loggedInUser")
                .get()
                .addOnSuccessListener { currentUserDoc ->
                    val friendsArray = currentUserDoc.get("friends") as? List<Long> ?: emptyList()

                    usersRef.whereEqualTo("username", post.postOP)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val targetUserDoc = querySnapshot.documents[0]
                            val targetUserID = targetUserDoc.getLong("userID")

                            if (targetUserID != null) {
                                //check for if postOP is the logged in user
                                if (targetUserID.toInt() == loggedInUser) {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "You cannot add yourself as a friend.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else if (friendsArray.contains(targetUserID)) {
                                    //check for if postOP already a friend
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Already friends with ${post.postOP}.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    // add user to friends list
                                    usersRef.document("$loggedInUser")
                                        .update(
                                            "friends",
                                            com.google.firebase.firestore.FieldValue.arrayUnion(
                                                targetUserID))
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                holder.itemView.context,
                                                "Added ${post.postOP} as a friend.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            }
                        }
                }
        }
        holder.addFriendText.setOnClickListener {
            val db = Firebase.firestore
            val usersRef = db.collection("users")

            //get friendsArray
            usersRef.document("$loggedInUser")
                .get()
                .addOnSuccessListener { currentUserDoc ->
                    val friendsArray = currentUserDoc.get("friends") as? List<Long> ?: emptyList()

                    usersRef.whereEqualTo("username", post.postOP)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val targetUserDoc = querySnapshot.documents[0]
                            val targetUserID = targetUserDoc.getLong("userID")

                            if (targetUserID != null) {
                                //check for if postOP is the logged in user
                                if (targetUserID.toInt() == loggedInUser) {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "You cannot add yourself as a friend.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else if (friendsArray.contains(targetUserID)) {
                                    //check for if postOP already a friend
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Already friends with ${post.postOP}.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    // add user to friends list
                                    usersRef.document("$loggedInUser")
                                        .update(
                                            "friends",
                                            com.google.firebase.firestore.FieldValue.arrayUnion(
                                                targetUserID))
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                holder.itemView.context,
                                                "Added ${post.postOP} as a friend.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            }
                        }
                }
        }

        // real button too hard make text image clickingable instead
        holder.commentImage.setOnClickListener {
            onItemClick(post)
        }
        holder.commentText.setOnClickListener {
            onItemClick(post)
        }

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class HomeViewHolder(itemView: View, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

        // have to comment this out otherwise an animation would play when clicking on cardview
//        init {
//            itemView.setOnClickListener {
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    onItemClicked(position)
//                }
//            }
//        }
        val postClue: TextView = itemView.findViewById(R.id.postClue)
        val postOP: TextView = itemView.findViewById(R.id.postOP)
        val postRatingStars: RatingBar = itemView.findViewById(R.id.postRatingStars)
        val addFriendImage: ImageView = itemView.findViewById(R.id.addFriendImage)
        val addFriendText: TextView = itemView.findViewById(R.id.addFriendText)
        val commentImage: ImageView = itemView.findViewById(R.id.commentImage)
        val commentText: TextView = itemView.findViewById(R.id.commentText)

    }
    }