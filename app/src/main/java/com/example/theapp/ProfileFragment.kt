package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.theapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : BottomSheetDialogFragment() {
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

        binding.closeProfileButton.setOnClickListener { view ->
            dismiss()
        }

        binding.recentProgress.setOnClickListener { view ->
            val navController = requireActivity()
                .findNavController(R.id.nav_host_fragment_content_main)

            navController.navigate(R.id.RecentProgressFragment)
            dismiss()
        }

        binding.logOut.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.log_out).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}