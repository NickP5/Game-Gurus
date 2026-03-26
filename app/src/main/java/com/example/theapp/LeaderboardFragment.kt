package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.theapp.databinding.FragmentLeaderboardBinding

class LeaderboardFragment : Fragment() {
    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = listOf(
            Triple(1, "Jonathan", 20),
            Triple(2, "Jon", 19),
            Triple(3, "John", 18),
            Triple(4, "Johnathan", 17),
            Triple(5, "Johnn", 16),
            Triple(6, "Johnny", 15),
            Triple(7, "Jony", 14),
            Triple(8, "Jonelle", 13),
            Triple(9, "Joni", 12),
            Triple(10, "Joaehen", 11),
            Triple(11, "Joe", 10),
            Triple(12, "Joesph", 9),
            Triple(13, "Jobe", 8),
            Triple(14, "Josh", 7),
            Triple(15, "Joshua", 6),
            Triple(16, "Joshin", 5),
            Triple(17, "Jovi", 4),
            Triple(18, "Jootle", 3),
            Triple(19, "Jiggle", 2),
            Triple(20, "Jonggles", 1)
        )
        val adapter = LeaderboardAdapter(data, requireContext())

        binding.leaderboardList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}