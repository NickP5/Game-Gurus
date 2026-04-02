package com.example.theapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.theapp.databinding.FragmentCreateBinding
import com.example.theapp.databinding.FragmentLoginBinding

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

            //eventually only run this is ue/pass check works
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

    }

}