package com.bangkit.android.dermatify.ui.learn

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.FragmentLearnBinding
import com.bangkit.android.dermatify.ui.adapter.ArticlesAdapter
import com.bangkit.android.dermatify.ui.adapter.ListArticleAdapter

class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!

    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var listArticleAdapter: ListArticleAdapter
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
        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        articlesAdapter = ArticlesAdapter(ArticlesAdapter.LEARN_BOT, findNavController(), requireContext())
        listArticleAdapter = ListArticleAdapter(ListArticleAdapter.LISTLEARN)
        concatAdapter = ConcatAdapter(articlesAdapter, listArticleAdapter)
        binding.apply {
            rvArticles.layoutManager = LinearLayoutManager(context)
            rvArticles.adapter = concatAdapter
        }
    }

}