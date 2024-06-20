package com.bangkit.android.dermatify.ui.home

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.databinding.FragmentHomeBinding
import com.bangkit.android.dermatify.ui.adapter.ArticlesAdapter
import com.bangkit.android.dermatify.ui.adapter.HeaderAdapter
import com.bangkit.android.dermatify.util.showSnackbarWithActionBtn

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeHeaderAdapter: HeaderAdapter
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var concatAdapter: ConcatAdapter

    private var userName = ""

    private val homeViewModel by activityViewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

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
        initUserNameObserver()
        initArticlesObserver()
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
        homeHeaderAdapter = HeaderAdapter(HeaderAdapter.HOME, findNavController(), requireContext(), userName)
        articlesAdapter = ArticlesAdapter(ArticlesAdapter.HIGHLIGHTS, context = requireContext())
        concatAdapter = ConcatAdapter(homeHeaderAdapter, articlesAdapter)
        binding.apply {
            rvHome.layoutManager = LinearLayoutManager(context)
            rvHome.adapter = concatAdapter
        }
    }

    private fun initUserNameObserver() {
        homeViewModel.getUserName().observe(viewLifecycleOwner) {
            removeAdapterFromConcat()
            homeHeaderAdapter.name = it
            addAdapterToConcat()
            concatAdapter.notifyDataSetChanged()
            Log.d("Cilukba", "dalam observe $it")
        }
    }


    private fun removeAdapterFromConcat() {
        concatAdapter.removeAdapter(homeHeaderAdapter)
        concatAdapter.removeAdapter(articlesAdapter)
    }

    private fun addAdapterToConcat() {
        concatAdapter.addAdapter(homeHeaderAdapter)
        concatAdapter.addAdapter(articlesAdapter)
    }

    private fun initArticlesObserver() {
        homeViewModel.articles.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Success -> {
                    Log.d("Cilukba", "fetch success")
                    concatAdapter.removeAdapter(articlesAdapter)
                    articlesAdapter = ArticlesAdapter(
                        tabType = ArticlesAdapter.HIGHLIGHTS,
                        context = requireContext(),
                        articles = result.data as List<ArticlesItem>
                    )
                    concatAdapter.addAdapter(articlesAdapter)
                    concatAdapter.notifyDataSetChanged()
                }
                is ApiResponse.Error -> {
                    Log.d("Cilukba", "fetch error")
                    concatAdapter.removeAdapter(articlesAdapter)
                    articlesAdapter = ArticlesAdapter(
                        tabType = ArticlesAdapter.HIGHLIGHTS,
                        context = requireContext()
                    )
                    concatAdapter.addAdapter(articlesAdapter)
                    concatAdapter.notifyDataSetChanged()
                    binding.rvHome.showSnackbarWithActionBtn(
                        message = getString(R.string.network_lost_2),
                        type = "error",
                        actionMsg = getString(R.string.try_again),
                        onClick = {
                            homeViewModel.fetchArticles()
                        }
                    )
                }
                is ApiResponse.Loading -> {
                    Log.d("Cilukba", "fetch loading")
                    concatAdapter.removeAdapter(articlesAdapter)
                    articlesAdapter = ArticlesAdapter(
                        tabType = ArticlesAdapter.HIGHLIGHTS,
                        context = requireContext()
                    )
                    concatAdapter.addAdapter(articlesAdapter)
                    concatAdapter.notifyDataSetChanged()
                }
            }
        }

    }



}