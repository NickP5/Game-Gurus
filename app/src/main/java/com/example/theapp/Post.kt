package com.example.theapp

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Filter

public const val TAG = "Post"
public var pID = 0
public var uID = 0



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
        val documentID2 = uID
        val randomNumber = (1..100).random()

        val emails = listOf("notch@minecraft.net", "ap@mail.com", "nick@mail.com", "blingus@mail.com")
        val passwords = listOf("1234", "4321", "nickHnzi", "abcd")

        val userpost = hashMapOf(
            "name" to postOP,
            "rating" to postRating,
            "clue" to postClue,
            "pID" to pID

        )

        val user = hashMapOf(
            "username" to postOP,
            "pfp" to 0,
            "email" to emails[uID],
            "password" to passwords[uID],
            "userID" to uID,
            "points" to randomNumber
            //next step will be posts (list of pID's), friends, recent history / stats
        )
        val postsRef = db.collection("posts")
        val usersRef = db.collection("users")
        //initializing users database using four users
        usersRef
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty()) {
                    db.collection("users")
                        .document("$documentID2")
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: $postOP")
                        }
                } else {
                    Log.d(TAG, "Document already exists")
                }
            }

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
                        .set(userpost)
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
        uID += 1
        }

    }

