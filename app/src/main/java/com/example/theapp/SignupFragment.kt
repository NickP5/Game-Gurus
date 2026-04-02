package com.example.theapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.theapp.databinding.FragmentLoginBinding
import com.example.theapp.databinding.FragmentSignupBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore

private const val TAG = "SignUp"
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    lateinit var user_input: EditText
    lateinit var email_input: EditText
    lateinit var pass_input: EditText
    lateinit var pass_confirm: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_input = binding.root.findViewById(R.id.signupUser)
        email_input = binding.root.findViewById(R.id.signupEmail)
        pass_input = binding.root.findViewById(R.id.signupPass)
        pass_confirm = binding.root.findViewById(R.id.passConfirm)

        binding.signupButton.setOnClickListener {
            val user = user_input.text.toString()
            val email = email_input.text.toString()
            val pass = pass_input.text.toString()
            val confirm = pass_confirm.text.toString()

            //We're going to first go through all users looking for if a username or email matches
            //If either a username or email matches, Sign Up fails, and we tell the user that one of
            //the two is already in use
            val db = Firebase.firestore
            val userID = uID

            val userRef = db.collection("users")

            userRef
                .where(
                    Filter.or(
                        Filter.equalTo("username",user),
                        Filter.equalTo("email", email)
                    )
                ).get()
                .addOnSuccessListener { documentSnapshots ->
                    //checking password vs confirm
                    if (pass == confirm) {
                        //If there is nothing in documentSnapshot, it succeeded
                        if (documentSnapshots.isEmpty) {
                            val user = hashMapOf(
                                "email" to email,
                                "friends" to listOf<Int>(),
                                "pfp" to 0,
                                "userID" to userID,
                                "points" to 0,
                                "posts" to listOf<Int>(),
                                "username" to user,
                                "password" to confirm
                            )
                            //add user to database
                            userRef
                                .document("$userID")
                                .set(user)
                            Log.d(TAG, "User successfully added to database")
                            loggedInUser = userID
                            //eventually only run this is ue/pass check works
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            //Filter returned something, meaning that a user with the same username or email already exists
                            Log.d(TAG, "User with same username/email already exists")
                        }
                    } else {
                        //Passwords do not match, error popup
                        Log.d(TAG, "Passwords do not match")
                    }
                }
        }
    }
}