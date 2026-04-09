package com.example.theapp

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
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

    var postAnswer: String? = null


    constructor(postClue: String?, postRating: String?, postOP: String?, postAnswer: String?) {
        this.postClue = postClue
        this.postRating = postRating
        this.postOP = postOP
        this.postAnswer = postAnswer
    }


    fun saveUserToFirestore() {
        val db = Firebase.firestore
        val documentID = pID
        val documentID2 = uID
        val randomNumber = (1..100).random()
        val postsRef = db.collection("posts")
        val usersRef = db.collection("users")

        val emails = listOf("notch@minecraft.net", "ap@mail.com", "nick@mail.com", "blingus@mail.com")
        val passwords = listOf("1234", "4321", "nickHnzi", "abcd")

        val post = hashMapOf(
            "name" to postOP,
            "rating" to postRating,
            "clue" to postClue,
            "answer" to postAnswer,
            "pID" to documentID

        )

        val user = hashMapOf(
            "username" to postOP,
            "pfp" to 0,
            "email" to emails[uID % emails.size],
            "password" to passwords[uID % passwords.size],
            "userID" to documentID2,
            "points" to randomNumber,
            "posts" to listOf<Int>(),
            "friends" to listOf<Int>()
            //next step will be posts (list of pID's), friends, recent history / stats
        )

        //initializing users database using four users
        usersRef.document("$documentID2")
            .get()
            //If user doesn't exist, create document
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    usersRef
                        .document("$documentID2")
                        .set(user)
                        //Bug here, not adding new posts to same user post list
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: $postOP")
                        }
                } else {
                    Log.d(TAG, "Document: $documentID2 already exists")
                }
            }

        //if this v is document exists, fuck off and tell them no
        //else add document to post collection
        postsRef
            //.whereEqualTo("pID", pID).whereEqualTo("name", postOP).get()
            .where(
                Filter.or(
                Filter.equalTo("pID", documentID),
                Filter.equalTo("name", postOP)
                )
            ).get()
            .addOnSuccessListener { querySnapshot->
                if (querySnapshot.isEmpty()) {
                    db.collection("posts")
                        .document("$documentID")
                        .set(post)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Post added to Database")

                            usersRef.document("$documentID2")
                                .update("posts", FieldValue.arrayUnion(documentID))
                                .addOnSuccessListener {
                                    Log.d(TAG, "Post $documentID added to user $documentID2 list")
                                }
                        }
                } else {
                    Log.d(TAG, "Post already exists")
                }
            }
            .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        pID += 1
        uID += 1
        }

    }
