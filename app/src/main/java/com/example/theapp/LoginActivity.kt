package com.example.theapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.theapp.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore

private const val TAG = "LogIn"

public var loggedInUser = 0

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding


    lateinit var ue_input: EditText
    lateinit var pass_input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ue_input = binding.root.findViewById<EditText>(R.id.loginUE)
        pass_input = binding.root.findViewById<EditText>(R.id.loginPass)

        binding.loginButton.setOnClickListener {
            val ue = ue_input.text.toString()
            val pass = pass_input.text.toString()

            //We're going to first go through all users looking for if a username or email matches
            //what ue or pass has, only if that completes will we check for a correct password
            val db = Firebase.firestore

            val userRef = db.collection("users")

            //Query to get any usernames OR emails that match ue
            userRef
                .where(
                    Filter.or(
                        Filter.equalTo("username",ue),
                        Filter.equalTo("email", ue)
                    )
                ).get()
                .addOnSuccessListener { documentSnapshots ->
                    if (!documentSnapshots.isEmpty) {
                        val document = documentSnapshots.documents[0]
                        val correctPass = document.getString("password")

                        Log.d(TAG, "User exists in database, check for password")

                        if (correctPass == pass) {
                            Log.d(TAG, "Correct Password, Login Successful")
                            loggedInUser = document.get("userID") as Int

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                            // code to animate background
                            val drawable : AnimationDrawable = binding.root.background as AnimationDrawable

                            drawable.setEnterFadeDuration(1250)
                            drawable.setExitFadeDuration(2500)
                            drawable.start()
                        } else {
                            // where anything to display incorrect password would happen
                            Log.d(TAG, "Incorrect Password")
                            }
                    } else {
                        // This is where we would tell them that no user with that username/email exists
                         Log.d(TAG, "No user with that Username/Email")
                    }
                }
            }
        }
    }