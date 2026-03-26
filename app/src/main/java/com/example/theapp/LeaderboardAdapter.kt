package com.example.theapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.theapp.databinding.LeaderboardElementBinding

class LeaderboardAdapter(
    data: List<List<Any>>,
    context: Context,
    private val userName: String
) : ArrayAdapter<List<Any>>(context, 0, data) {

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

        val (username, rank, name, score) = getItem(position)!!
        val highlightBackgroundColor = ContextCompat.getDrawable(context, R.drawable.selected_roundedcorners)
        val backgroundColor = ContextCompat.getDrawable(context, R.drawable.roundedcorners)
        val textColor = ContextCompat.getColor(context, R.color.primary)

        if (username == userName) {
            binding.leaderboardListItem.background = highlightBackgroundColor
            binding.rankText.setTextColor(Color.BLACK)
            binding.nameText.setTextColor(Color.BLACK)
            binding.pointsText.setTextColor(Color.BLACK)
        } else {
            binding.leaderboardListItem.background = backgroundColor
            binding.rankText.setTextColor(textColor)
            binding.nameText.setTextColor(textColor)
            binding.pointsText.setTextColor(textColor)
        }

        binding.rankText.text = rank.toString()
        binding.nameText.text = name.toString()
        binding.pointsText.text = score.toString()

        return binding.root
    }
}