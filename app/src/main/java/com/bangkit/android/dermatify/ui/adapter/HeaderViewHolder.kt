package com.bangkit.android.dermatify.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bangkit.android.dermatify.databinding.ItemHeaderHomeBinding

sealed class HeaderViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class HomeHeaderViewHolder(private val binding: ItemHeaderHomeBinding) : HeaderViewHolder(binding) {
        fun bind() {
            binding.apply {

            }
        }
    }
}