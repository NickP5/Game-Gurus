package com.example.theapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.theapp.databinding.FragmentLoginBinding
import com.example.theapp.databinding.FragmentSignupBinding

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
            val email = user_input.text.toString()
            val pass = pass_input.text.toString()
            val confirm = pass_confirm.text.toString()

            //eventually only run this is ue/pass check works
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}