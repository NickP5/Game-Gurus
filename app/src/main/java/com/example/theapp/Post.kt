package com.example.theapp

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.SetOptions

private const val TAG = "Post"
public var pID = 0



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
        val documentID = pID

        val user = hashMapOf(
            "name" to postOP,
            "rating" to postRating,
            "clue" to postClue,
            "pID" to pID

        )
        val postsRef = db.collection("posts")
        val query = postsRef.whereEqualTo("clue", postClue)
        //Log.d(TAG, "Documents count: $count"
        //if this v is document exists, fuck off and tell them no
        //else add document to post collection
        db.collection("posts")
            .whereEqualTo("pID", pID).whereEqualTo("name", postOP).get()
            .addOnSuccessListener { documentSnapshot->
                if (!documentSnapshot.exists()) {}


        db.collection("posts")
            .document("$documentID").set(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                }
            .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        pID += 1
        }

    }

