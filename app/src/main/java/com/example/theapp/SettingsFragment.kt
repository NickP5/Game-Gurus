package com.example.theapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate

import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.edit

const val PREFS_NAME = "settings"
const val KEY_DARK_MODE = "DARK_MODE"

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean(KEY_DARK_MODE, false)

        val savedScale = sharedPref.getFloat("font_scale", 1.0f)

        val spinnerPosition = when (savedScale) {
            0.85f -> 0
            1.0f -> 1
            1.3f -> 2
            else -> 1
        }

        binding.switchDarkMode.isChecked = isDarkMode
        setDarkMode(isDarkMode)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.font_size_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.fontSpinner.adapter = adapter
        }
        binding.fontSpinner.setSelection(spinnerPosition)

        binding.accountPreferences.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.accountPreferences).show()
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit {
                putBoolean(KEY_DARK_MODE, isChecked)
            }
            setDarkMode(isChecked)
        }

        var isUserInteraction = false

        binding.fontSpinner.setOnTouchListener { _, _ ->
            isUserInteraction = true
            false
        }

        binding.fontSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!isUserInteraction) return

                val newScale = when(position) {
                    0 -> 0.85f
                    1 -> 1.0f
                    2 -> 1.3f
                    else -> 1.0f
                }

                val currentScale = sharedPref.getFloat("font_scale", 1.0f)
                if (newScale != currentScale) {
                    sharedPref.edit { putFloat("font_scale", newScale) }
                    requireActivity().recreate()
                }
                isUserInteraction = false
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    fun setDarkMode(enabled: Boolean) {
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}