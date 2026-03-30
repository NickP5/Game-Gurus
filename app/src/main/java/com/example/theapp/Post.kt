package com.example.theapp

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Filter

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

        //if this v is document exists, fuck off and tell them no
        //else add document to post collection
        postsRef
            //.whereEqualTo("pID", pID).whereEqualTo("name", postOP).get()
            .where(
                Filter.or(
                Filter.equalTo("pID", pID),
                Filter.equalTo("name", postOP)
                )
            ).get()
            .addOnSuccessListener { querySnapshot->
                if (querySnapshot.isEmpty()) {
                    db.collection("posts")
                        .document("$documentID")
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                        }
                } else {
                    Log.d(TAG, "Document already exists")
                }
            }
            .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        pID += 1
        }

    }

