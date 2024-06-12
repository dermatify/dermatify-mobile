package com.bangkit.android.dermatify.ui.home

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentHomeBinding
import com.bangkit.android.dermatify.ui.adapter.ArticlesAdapter
import com.bangkit.android.dermatify.ui.adapter.HeaderAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeHeaderAdapter: HeaderAdapter
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
        requireActivity().window.navigationBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    // Ensure the status bar and nav bar color is
    // the same as backround when popback from Profile Fragment
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
        requireActivity().window.navigationBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
    }

    private fun initUI() {
        homeHeaderAdapter = HeaderAdapter(HeaderAdapter.HOME, findNavController())
        articlesAdapter = ArticlesAdapter(ArticlesAdapter.HIGHLIGHTS)
        concatAdapter = ConcatAdapter(homeHeaderAdapter, articlesAdapter)
        binding.apply {
            rvHome.layoutManager = LinearLayoutManager(context)
            rvHome.adapter = concatAdapter
        }
    }

    private fun setupHeaderRV() {
        homeHeaderAdapter = HeaderAdapter(HeaderAdapter.HOME, findNavController())
    }

}