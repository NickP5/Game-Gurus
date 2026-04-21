package com.example.theapp

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.theapp.databinding.LeaderboardElementBinding

private const val TAG4 = "Leaderboard"
class LeaderboardAdapter(
    private var data: List<LeaderboardEntry>?,
    private val context: Context
) : BaseAdapter() {

    override fun getCount() = data?.size ?: 0
    override fun getItem(position: Int) = data?.get(position)
    override fun getItemId(position: Int) = position.toLong()

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

        val item = data?.get(position)
        val firstLetter = item?.firstLetter
        val rank = item?.rank
        val name = item?.username
        val score = item?.points
        val id = item?.userID
        val highlightBackgroundColor = ContextCompat.getDrawable(context, R.drawable.selected_roundedcorners)
        val backgroundColor = ContextCompat.getDrawable(context, R.drawable.roundedcorners)
        val textColor = ContextCompat.getColor(context, R.color.primary)

//        Log.d(TAG4, "userName is: $userName , name is: $name")

        if (loggedInUser == id) {
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

    fun updateData(newData: List<LeaderboardEntry>?) {
        data = newData
        notifyDataSetChanged()
    }
}