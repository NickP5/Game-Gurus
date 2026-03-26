package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

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

        clueInput = view.findViewById(R.id.clueField)
        ratingInput = view.findViewById(R.id.ratingField)

        binding.postButton.setOnClickListener {
            val clue = clueInput.text.toString()
            val rating = ratingInput.text.toString()
            val name = "hi nick hnzi" // replace with the user's name

            val newPost = Post(clue, rating, name)
            newPost.saveUserToFirestore()

            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}