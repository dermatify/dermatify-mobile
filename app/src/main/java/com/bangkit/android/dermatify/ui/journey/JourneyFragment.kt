package com.bangkit.android.dermatify.ui.journey

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.databinding.FragmentJourneyBinding
import com.bangkit.android.dermatify.ui.adapter.JourneyAdapter


class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null
    private val binding get() = _binding!!
    private lateinit var journeyAdapter: JourneyAdapter
    private val viewModel by activityViewModels<JourneyViewModel> {
        JourneyViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
        requireActivity().window.navigationBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data : List<Scans> = listOf(
            Scans(13, "iimg", "desc", "June", "rednes"),
            Scans(34, "lmg", "descc", "July", "acnes")
        )

        setupRecyclerView(data)

//        viewModel.getAllHistories().observe(viewLifecycleOwner) {scans ->
//            if ((viewModel.size.value ?: 0) > 0) {
//                setupRecyclerView(scans)
//                journeyAdapter.submitList(scans)
//            }
//        }

        setupDropDownMenus()
        observerViewModel()
        viewModel.refreshData()

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
                val selectedMonth = resources.getStringArray(R.array.months_array)[position]
                viewModel.refreshDataByMonth(selectedMonth)
                Log.d("Dropdown", "load")
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
                viewModel.refreshDataByWeek(selectedWeek)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupRecyclerView(scans: List<Scans>) {
        journeyAdapter = JourneyAdapter(scans)
        binding.rvJourney.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = journeyAdapter
        }
    }

    private fun observerViewModel() {
        viewModel.historyResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Error -> {
                    Log.e("JourneyFragment", "Error: ${response.errorMsg}")
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.errorMsg}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ApiResponse.Loading -> {
                    Log.d("JourneyFragment", "Loading data...")
                }
                is ApiResponse.Success -> {
                    Log.d("JourneyFragment", "Data loaded successfully")
                    journeyAdapter.submitList(response.data)
                }

            }
        }
        viewModel.historyByMonth.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Error -> {
                    Log.e("JourneyFragment", "Error: ${response.errorMsg}")
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.errorMsg}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ApiResponse.Loading -> {
                    Log.d("JourneyFragment", "Loading data...")
                }
                is ApiResponse.Success -> {
                    Log.d("JourneyFragment", "Data loaded successfully")
                    journeyAdapter.submitList(response.data)
                }

            }
        }
        viewModel.historyByWeek.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Error -> {
                    Log.e("JourneyFragment", "Error: ${response.errorMsg}")
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.errorMsg}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ApiResponse.Loading -> {
                    Log.d("JourneyFragment", "Loading data...")
                }
                is ApiResponse.Success -> {
                    Log.d("JourneyFragment", "Data loaded successfully")
                    journeyAdapter.submitList(response.data)
                }

            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}