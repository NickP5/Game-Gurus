package com.example.theapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.theapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


private const val ProfileTAG = "Profile"
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

        // loggedInUser and get username
        val db = Firebase.firestore
        val docRef = db.collection("users")
        //Not working for some reason.
        var nameString = ""

        //Getting name of loggInUser
        docRef.document("$loggedInUser").get()
            .addOnSuccessListener { documentSnapshot ->
                nameString = documentSnapshot.getString("username").toString()
                Log.d(ProfileTAG, "Got nameString: $nameString")
        Log.d(ProfileTAG, "Real nameString: $nameString")

        binding.displayNameText.text = nameString
    }
        // Requirement for switching outside of profile fragment
        val navController = requireActivity()
            .findNavController(R.id.nav_host_fragment_content_main)



        // Temporary Profile Image
        binding.profilePic.setImageResource(R.drawable.mountain)

        binding.closeProfileButton.setOnClickListener { view ->
            dismiss()
        }

        binding.statistics.setOnClickListener { view ->
            navController.navigate(R.id.StatisticsFragment)
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