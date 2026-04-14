package com.example.theapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

        // Profile picture options
        val pfpOptions = listOf(
            "black_mountain.jpg",
            "mountain.jpg",
            "mountains_minimal_background_78370_9589.jpg",
            "nyan_cat_cartoon_video_games_wallpaper_preview.jpg"
        )
        val pfpDrawables = mapOf(
            0 to R.drawable.black_mountain,
            1 to R.drawable.mountain,
            2 to R.drawable.mountains_minimal_background_78370_9589,
            3 to R.drawable.nyan_cat_cartoon_video_games_wallpaper_preview
        )

        // Set up the spinner
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pfpOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.pfpSpinner.adapter = adapter

        //Getting name of loggInUser
        docRef.document("$loggedInUser").get()
            .addOnSuccessListener { documentSnapshot ->
                nameString = documentSnapshot.getString("username").toString()
                Log.d(ProfileTAG, "Got nameString: $nameString")
                Log.d(ProfileTAG, "Real nameString: $nameString")
                val displayName = "Blingus"
                val username = "Blingus"

                binding.displayNameText.text = displayName


                // Load saved profile picture if it exists
                val savedPfp = documentSnapshot.getString("pfp")
                savedPfp?.let {
                    val index = pfpOptions.indexOf(it)
                    if (index >= 0) {
                        binding.pfpSpinner.setSelection(index, false)
                        binding.profilePic.setImageResource(pfpDrawables[index]!!)
                    }
                }
            }
        //Log.d(ProfileTAG, "Real nameString: $nameString")

        binding.displayNameText.text = nameString

        // Handle profile picture change
        binding.pfpSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedPfp = pfpOptions[position]
                val drawableId = pfpDrawables[position]

                // Update UI
                binding.profilePic.setImageResource(drawableId!!)

                // Save to Firestore
                docRef.document("$loggedInUser")
                    .update("pfp", selectedPfp)
                    .addOnSuccessListener {
                        Log.d(ProfileTAG, "Profile picture updated in Firestore to $selectedPfp")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ProfileTAG, "Error updating profile picture", e)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            // Requirement for switching outside of profile fragment
            val navController = requireActivity()
                .findNavController(R.id.nav_host_fragment_content_main)

                // Temporary Profile Image

                binding.statistics.setOnClickListener { view ->
                    navController.navigate(R.id.StatisticsFragment)
                }

                binding.notifications.setOnClickListener { view ->
                    dismiss()
                    navController.navigate(R.id.NotificationsFragment)
                }

                binding.closeProfileButton.setOnClickListener { view ->
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
