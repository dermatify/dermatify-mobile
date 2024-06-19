package com.bangkit.android.dermatify.ui.learn

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.databinding.FragmentLearnBinding
import com.bangkit.android.dermatify.ui.adapter.ArticlesAdapter
import com.bangkit.android.dermatify.ui.adapter.HeaderAdapter
import com.bangkit.android.dermatify.ui.adapter.HeaderAdapter.Companion.LEARN_TOP
import com.bangkit.android.dermatify.ui.adapter.ListArticleAdapter
import com.bangkit.android.dermatify.util.showSnackbarWithActionBtn

class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!

    private val learnViewModel by activityViewModels<LearnViewModel> {
        ViewModelFactory.getInstance()
    }

    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var topHeaderAdapter: HeaderAdapter
    private lateinit var botHeaderAdapter: HeaderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
        requireActivity().window.navigationBarColor = activity?.getColor(R.color.white_background) ?: Color.TRANSPARENT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initArticlesObserver()
    }


    private fun initUI() {
        topHeaderAdapter = HeaderAdapter(
            tabType = LEARN_TOP,
            context = requireContext()
        )
        botHeaderAdapter = HeaderAdapter(
            tabType = HeaderAdapter.LEARN_BOT,
            context = requireContext()
        )
        articlesAdapter = ArticlesAdapter(
            tabType = ArticlesAdapter.LEARN_BOT,
            context = requireContext()
        )
        concatAdapter = ConcatAdapter(topHeaderAdapter, botHeaderAdapter, articlesAdapter)
        binding.apply {
            rvArticles.layoutManager = LinearLayoutManager(context)
            rvArticles.adapter = concatAdapter
        }
    }

    private fun initArticlesObserver() {
        learnViewModel.articles.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Error -> {
                    Log.d("Cilukba", "fetch error")

                    binding.rvArticles.showSnackbarWithActionBtn(
                        message = getString(R.string.network_lost_2),
                        type = "error",
                        actionMsg = getString(R.string.try_again),
                        onClick = {
                            learnViewModel.fetchArticles()
                        }
                    )
                }

                is ApiResponse.Loading -> {
                    Log.d("Cilukba", "fetch loading")
                }
                is ApiResponse.Success -> {
                    Log.d("Cilukba", "fetch success")

                    val topArticles = (result.data as List<ArticlesItem>).subList(0,4)
//                    val botArticles = (result.data as List<ArticlesItem>).subList(4,13)

                    concatAdapter.removeAdapter(topHeaderAdapter)
                    concatAdapter.removeAdapter(botHeaderAdapter)
                    concatAdapter.removeAdapter(articlesAdapter)

                    topHeaderAdapter = HeaderAdapter(
                        tabType = LEARN_TOP,
                        context = requireContext(),
                        articles = topArticles
                    )
                    botHeaderAdapter = HeaderAdapter(
                        tabType = HeaderAdapter.LEARN_BOT,
                        context = requireContext()
                    )
                    articlesAdapter = ArticlesAdapter(
                        tabType = ArticlesAdapter.LEARN_BOT,
                        context = requireContext(),
//                        articles = botArticles
                    )
                    concatAdapter.addAdapter(topHeaderAdapter)
                    concatAdapter.addAdapter(botHeaderAdapter)
                    concatAdapter.addAdapter(articlesAdapter)

                    concatAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}