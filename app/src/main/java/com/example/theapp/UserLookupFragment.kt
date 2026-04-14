package com.example.theapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.databinding.FragmentUserLookupBinding

class UserLookupFragment : Fragment() {
    private var _binding: FragmentUserLookupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLookupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allUsers = mutableListOf(
            User(0, "Jarvis", 0),
            User(1, "Hulk", 0),
            User(2, "Mark", 0),
            User(3, "Joggins", 0),
            User(4, "Liasas", 0),
            User(5, "JumpingWilly", 0),
            User(6, "Rad", 0),
            User(7, "Poaster", 0),
            User(8, "Batman", 0),
            User(9, "IronMan", 0),
            User(10, "CaptainAmerica", 0)
        )
        val fullList = allUsers

        val adapter = UserAdapter()

        val recyclerView: RecyclerView = view.findViewById(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = adapter

        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString()

            val filtered = if (query.isEmpty()) {
                emptyList<User>()
            } else {
                fullList.filter {
                    it.username?.contains(query, ignoreCase = true) == true
                }
            }
            adapter.submitList(filtered)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}