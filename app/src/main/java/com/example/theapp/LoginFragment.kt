package com.example.theapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.theapp.databinding.FragmentCreateBinding
import com.example.theapp.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore

public var loggedInUser = 0

private const val TAG2 = "Login"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    lateinit var ue_input: EditText
    lateinit var pass_input: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        //If there is something in the snapshot, we assume it succeeded
                        if (!documentSnapshots.isEmpty) {
                            val document = documentSnapshots.documents[0]
                            val correctPass = document.getString("password")

                            Log.d(TAG2, "User exists in database, check for password")

                            if (correctPass == pass) {
                                Log.d(TAG2, "Correct Password, Login Successful")
                                loggedInUser = document.getLong("userID")?.toInt() ?: 0

                                Log.d(TAG2, "Logged in user: $loggedInUser")
                                
                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                // where anything to display incorrect password would happen
                                Log.d(TAG2, "Incorrect Password")
                            }
                        } else {
                            // This is where we would tell them that no user with that username/email exists
                            Log.d(TAG2, "No user with that Username/Email")
                        }
                    }
            }
        }
    }