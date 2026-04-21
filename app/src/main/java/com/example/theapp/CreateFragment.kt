package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.theapp.databinding.FragmentCreateBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    lateinit var gameInput: EditText
    lateinit var clueInput: EditText
    lateinit var ratingBar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameInput = view.findViewById(R.id.gameField)
        clueInput = view.findViewById(R.id.clueField)
        ratingBar = view.findViewById(R.id.ratingBar)

        val db = Firebase.firestore
        val docRef = db.collection("users")
        var nameString = ""

        docRef.document("$loggedInUser").get()
            .addOnSuccessListener { documentSnapshot ->
                nameString = documentSnapshot.getString("username").toString()
            }

        binding.postButton.setOnClickListener {
            val game = gameInput.text.toString() // will discuss use case in class
            val clue = clueInput.text.toString()
            val stars = ratingBar.rating.toString()
            val name = nameString // replace with the user's name

            if (game.isNotBlank() && clue.isNotBlank() && stars.isNotBlank()) {
                val newPost = Post(clue, stars, name, game)
                newPost.savePostToFirestore()

                Snackbar.make(view, "Successfully Added Post!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.bottomNavigationView).show()
                findNavController().navigate(R.id.HomeFragment)
            } else {
                Snackbar.make(view, "Text field cannot be left blank", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.bottomNavigationView).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}