package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentCreateBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNav: BottomNavigationView

    lateinit var gameInput: EditText
    lateinit var clueInput: EditText
    lateinit var ratingInput: EditText

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

        bottomNav = requireActivity().findViewById(R.id.bottomNavigationView)
        ViewCompat.setOnApplyWindowInsetsListener(requireActivity().findViewById(android.R.id.content)) { _, insets ->
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

            bottomNav.isVisible = !isKeyboardVisible

            insets
        }

        gameInput = view.findViewById(R.id.gameField)
        clueInput = view.findViewById(R.id.clueField)
        ratingInput = view.findViewById(R.id.ratingField)


        binding.postButton.setOnClickListener {
            val game = gameInput.text.toString() // will discuss use case in class
            val clue = clueInput.text.toString()
            val rating = ratingInput.text.toString()
            val name = "hi nick hnzi" // replace with the user's name

            val newPost = Post(clue, rating, name, game)
            newPost.saveUserToFirestore()

            Snackbar.make(view, "Post Added!", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.bottomNavigationView).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}