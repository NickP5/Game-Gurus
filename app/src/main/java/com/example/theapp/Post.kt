package com.example.theapp

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.util.Log

private const val TAG = "Post"



class Post {
    // will likely need to add a postId and comments here,
    // this implementation is mainly for testing recyclerView
    var postClue: String? = null
    var postRating: String? = null // will need to discuss rating system a bit further
    var postOP: String? = null


    constructor(postClue: String?, postRating: String?, postOP: String?) {
        this.postClue = postClue
        this.postRating = postRating
        this.postOP = postOP
    }


    fun saveUserToFirestore() {
        val db = Firebase.firestore

        val user = hashMapOf(
            "name" to postOP,
            "rating" to postRating,
            "clue" to postClue
        )

        db.collection("posts")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}

