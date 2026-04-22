package com.example.theapp

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.PropertyName

public const val TAG = "Post"

public const val TAG5 = "User"
public const val TAG6 = "Post2"
public var pID = 0
public var uID = 0



class Post {
    // will likely need to add a postId and comments here,
    // this implementation is mainly for testing recyclerView
    @get:PropertyName("clue") @set:PropertyName("clue")
    var postClue: String? = null
    @get:PropertyName("rating") @set:PropertyName("rating")
    var postRating: String? = null // will need to discuss rating system a bit further
    @get:PropertyName("name") @set:PropertyName("name")
    var postOP: String? = null
    @get:PropertyName("answer") @set:PropertyName("answer")
    var postAnswer: String? = null
    @get:PropertyName("pID") @set:PropertyName("pID")
    var postID: Int? = null

    constructor()


    constructor(postClue: String?, postRating: String?, postOP: String?, postAnswer: String?) {
        this.postClue = postClue
        this.postRating = postRating
        this.postOP = postOP
        this.postAnswer = postAnswer
    }

    fun getDbCount(database: CollectionReference, callback: (Long) -> Unit) {
        val countQuery = database.count()

        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val count = snapshot.count
                Log.d(TAG6, "Total count: $count")

                // Send the result back via the callback
                callback(count)
            } else {
                Log.e(TAG6, "Count failed: ", task.exception)
                callback(0L) // Or handle error
            }
            //How to call it
//            getDbCount(db.collection("posts")) { count ->
//                // Use the count here    println("The count is: $count")
//                // You could call savePostToFirestore(count) here
//            }
        }
    }

    fun getUserID(name: String, callback: (Int) -> Unit){
        val db = Firebase.firestore
        db.collection("users")
            .whereEqualTo("username", name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Get the first document found with that username
                    val document = querySnapshot.documents[0]
                    // Firestore numbers are Long, so we cast to Long then convert to Int
                    val userID = (document.get("userID") as? Long)?.toInt() ?: -1
                    callback(userID)
                } else {
                    Log.d(TAG5, "No user found with username: $name")
                    callback(-1) // Return -1 if user doesn't exist
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG5, "Error finding userID", e)
                callback(-1)
            }
    }

    fun savePostToFirestore() {
        //should be used only by the create post button
        val db = Firebase.firestore

        getDbCount(db.collection("posts")) { count ->

            val documentID = count.toInt()
            pID = documentID
            val documentID2 = uID

            val post = hashMapOf(
                "name" to postOP,
                "rating" to postRating,
                "clue" to postClue,
                "answer" to postAnswer,
                "pID" to documentID

            )

            val postsRef = db.collection("posts")
            val usersRef = db.collection("users")

            postsRef
                //.where(Filter.or(Filter.equalTo("pID", pID)))
                //checking if post exists by using and statement against pID, postClue, and postRating
                //.whereEqualTo("pID", pID)
                .whereEqualTo("clue", postClue)
                .whereEqualTo("rating", postRating).get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        db.collection("posts")
                            .document("$documentID")
                            .set(post)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG6, "Post added to Database")

                                usersRef.document("$loggedInUser")
                                    .update("posts", FieldValue.arrayUnion(documentID))
                                    .addOnSuccessListener {
                                        Log.d(
                                            TAG6, "Post $documentID added to logged in user list"
                                        )
                                    }
                            }
                    } else {
                        Log.d(TAG6, "Post already exists with pID, $documentID")
                    }
                }
        }
    }

    fun saveUserToFirestore() {
        val db = Firebase.firestore

        val documentID = pID
        val documentID2 = uID
        val randomNumber = (1..100).random()
        val postsRef = db.collection("posts")
        val usersRef = db.collection("users")

        val emails =
            listOf("notch@minecraft.net", "ap@mail.com", "nick@mail.com", "blingus@mail.com")
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


        //Currently we are attempting to add both the four test posts along with their
        //respective users, I am going to readd them once more then change the logic to
        //simply take the database information about posts to display them

        //initializing users database using four users
        //getDbCount(db.collection("users")) { userDbCount ->
        //documentID2 = userDbCount.toInt()
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
                            Log.d(TAG5, "User added with ID: $documentID2")
                            uID++
                        }
                } else {
                    Log.d(TAG5, "Document: $documentID2 already exists")
                    uID++
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG5, "Error adding document", e)
            }
        //}


        //if this v is document exists, fuck off and tell them no
        //else add document to post collection and then add the post to the
        //relevant user's post list

        //getDbCount(db.collection("posts")) { postDbCount ->
            //documentID = postDbCount.toInt()
            //pID = documentID
            Log.d(TAG, "pID is $pID")
            postsRef
                //.whereEqualTo("pID", pID).whereEqualTo("name", postOP).get()
                .where(
                    Filter.or(
                        Filter.equalTo("pID", documentID),
                        Filter.equalTo("name", postOP)
                    )
                ).get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        db.collection("posts")
                            .document("$documentID")
                            .set(post)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "Post added to Database")

                                //getDbCount(db.collection("users")) { userDbCount ->
                                //documentID2 = userDbCount.toInt()
                                //uID = documentID2
                                usersRef.document("$documentID2")
                                    .update("posts", FieldValue.arrayUnion(documentID))
                                    .addOnSuccessListener {
                                        Log.d(
                                            TAG,
                                            "Post $documentID added to user $documentID2 list"
                                        )
                                    }
                            }
                        //}
                    } else {
                        Log.d(TAG, "Post already exists, pID = $pID")
                        db.collection("posts")
                            .document("$documentID").get()
                            .addOnSuccessListener { documentReference ->

                                //getDbCount(db.collection("users")) { userDbCount ->
                                //documentID2 = userDbCount.toInt()
                                usersRef.document("$documentID2")
                                    .update("posts", FieldValue.arrayUnion(documentID))
                                Log.d(TAG, "Update user documents with postIDs")
                            }
                    }
                }
                //}
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        pID ++
        }
    }
//}
