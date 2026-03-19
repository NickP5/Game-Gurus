package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Temporary Strings
        val displayName = "Chip Peterson"
        val username = "@chippete"

        binding.displayNameText.text = displayName
        binding.usernameText.text = username

        // Temporary Profile Image
        binding.profilePic.setImageResource(R.drawable.mountain)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}