package com.example.theapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.theapp.databinding.LeaderboardElementBinding

class LeaderboardAdapter(
    data: List<Triple<Int, String, Int>>,
    context: Context
) : ArrayAdapter<Triple<Int, String, Int>>(context, 0, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: LeaderboardElementBinding
        if (convertView == null) {
            binding = LeaderboardElementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            binding.root.tag = binding
        } else {
            binding = convertView.tag as LeaderboardElementBinding
        }

        val (rank, name, score) = getItem(position)!!
        binding.rankText.text = rank.toString()
        binding.nameText.text = name
        binding.pointsText.text = score.toString()

        return binding.root
    }
}