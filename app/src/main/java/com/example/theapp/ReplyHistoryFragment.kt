package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theapp.databinding.FragmentReplyHistoryBinding

class ReplyHistoryFragment : Fragment() {
    private var _binding: FragmentReplyHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReplyHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val replyAdapter = ReplyAdapter(mutableListOf())

        binding.replyHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.replyHistoryRecyclerView.adapter = replyAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}