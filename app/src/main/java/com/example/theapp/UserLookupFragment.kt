package com.example.theapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theapp.databinding.FragmentUserLookupBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserLookupFragment : Fragment() {
    private var _binding: FragmentUserLookupBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNav: BottomNavigationView

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

        bottomNav = requireActivity().findViewById(R.id.bottomNavigationView)
        ViewCompat.setOnApplyWindowInsetsListener(requireActivity().findViewById(android.R.id.content)) { _, insets ->
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

            bottomNav.isVisible = !isKeyboardVisible

            insets
        }

        binding.searchEditText.requestFocus()
        binding.searchEditText.post {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        val allUsers = mutableListOf(
            User(0, "Jarvis", 0),
            User(1, "Hulk", 1),
            User(2, "Mark", 3),
            User(3, "Joggins", 0),
            User(4, "Liasas", 2),
            User(5, "JumpingWilly", 1),
            User(6, "Rad", 1),
            User(7, "Poaster", 3),
            User(8, "Batman", 3),
            User(9, "IronMan", 0),
            User(10, "CaptainAmerica", 2)
        )

        val adapter = UserAdapter()

        val recyclerView: RecyclerView = view.findViewById(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = adapter

        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString()

            val filtered = if (query.isEmpty()) {
                emptyList()
            } else {
                allUsers.filter {
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