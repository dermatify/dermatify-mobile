package com.bangkit.android.dermatify.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bangkit.android.dermatify.databinding.ItemArticlesBinding
import com.bangkit.android.dermatify.util.goneShimmer
import com.bangkit.android.dermatify.util.showShimmer

sealed class ArticlesViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class HighlightsArticlesViewHolder(private val binding: ItemArticlesBinding) : ArticlesViewHolder(binding) {
        fun bind(position: Int) {
            binding.apply {
                shimmerLayout.showShimmer()
            }
        }
    }
}