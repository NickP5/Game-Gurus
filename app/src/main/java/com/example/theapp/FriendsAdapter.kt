package com.example.theapp

import android.content.Context
import android.icu.text.Transliterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.theapp.databinding.FriendsItemBinding

class FriendsAdapter(
    data: List<String>,
    context: Context
) : ArrayAdapter<String>(context, 0, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: FriendsItemBinding
        if (convertView == null) {
            binding = FriendsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            binding.root.tag = binding
        } else {
            binding = convertView.tag as FriendsItemBinding
        }

        binding.nameText.text = getItem(position)!!

        return binding.root
    }
}