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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.local.room.HistoryDatabase
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.remote.retrofit.ApiConfig
import com.bangkit.android.dermatify.data.repository.JourneyRepository
import com.bangkit.android.dermatify.databinding.FragmentJourneyBinding
import com.bangkit.android.dermatify.ui.adapter.JourneyAdapter
import kotlinx.coroutines.launch

class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: JourneyAdapter
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

        setupDropDownMenus()
        setupRecyclerView()
        observerViewModel()

    }

    private fun setupDropDownMenus() {

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

        binding.dropdownMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedMonth = months[position]
                viewModel.getHistoriesByMonth(selectedMonth)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.dropdownWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedWeek = weeks[position]
                viewModel.getHistoriesByWeek(selectedWeek)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupRecyclerView() {
        adapter = JourneyAdapter { scan ->
            Toast.makeText(requireContext(), "Clicked: ${scan.diagnosis}", Toast.LENGTH_SHORT).show()
        }
        binding.rvJourney.layoutManager = LinearLayoutManager(requireContext())
        binding.rvJourney.adapter = adapter
    }

    private fun observerViewModel() {
        viewModel.historyResult.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is ApiResponse.Error -> {
                    Toast.makeText(requireContext(), "Error: ${response.errorMsg}", Toast.LENGTH_SHORT).show()
                }

                is ApiResponse.Loading -> {

                }

                is ApiResponse.Success -> {

                }

            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}