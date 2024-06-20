package com.bangkit.android.dermatify.ui.journey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.local.room.HistoryDatabase
import com.bangkit.android.dermatify.data.remote.retrofit.ApiConfig
import com.bangkit.android.dermatify.databinding.FragmentJourneyBinding
import kotlinx.coroutines.launch

class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JourneyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val months = resources.getStringArray(R.array.months_array)
        val weeks = resources.getStringArray(R.array.weeks_array)

        val monthAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdownMonth.adapter = monthAdapter

        val weekAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, weeks)
        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdownWeek.adapter = weekAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}