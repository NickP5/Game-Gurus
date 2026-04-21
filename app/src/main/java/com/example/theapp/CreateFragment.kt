package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentCreateBinding

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

        binding.postButton.setOnClickListener {
            val game = gameInput.text.toString() // will discuss use case in class
            val clue = clueInput.text.toString()
            val stars = ratingBar.rating.toString()
            val name = "hi nick hnzi" // replace with the user's name

            val newPost = Post(clue, stars, name)
            newPost.saveUserToFirestore()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}