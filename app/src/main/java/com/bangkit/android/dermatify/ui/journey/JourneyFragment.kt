package com.bangkit.android.dermatify.ui.journey

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.android.dermatify.R
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

        setupRecyclerView()
        observerViewModel()
        viewModel.refreshData()

    }

    private fun setupRecyclerView() {
        journeyAdapter = JourneyAdapter()
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
                    response.data?.let { scansList ->
                        journeyAdapter.setScans(scansList)
                    }
                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}