package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Temporary values for the statistics page
        val displayName = "Jonathan"
        val recentPoints = 9
        val recentAttempts = 90
        val recentPercentage = 10.0
        val totalPoints = 9
        val totalAttempts = 90
        val totalPercentage = 10.0

        binding.displayNameText.text = displayName
        binding.recentPoints.text = recentPoints.toString()
        binding.recentAttempts.text = recentAttempts.toString()
        binding.recentCorrectPercent.text = "$recentPercentage%"
        binding.totalPoints.text = totalPoints.toString()
        binding.totalAttempts.text = totalAttempts.toString()
        binding.totalCorrectPercent.text = "$totalPercentage%"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}