package com.bangkit.android.dermatify.ui.adapter

import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.ItemHeaderHomeBinding

sealed class HeaderViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class HomeHeaderViewHolder(private val binding: ItemHeaderHomeBinding, private val navController: NavController) : HeaderViewHolder(binding) {
        fun bind() {
            binding.apply {
                btnAccProfile.setOnClickListener {
                    navController.navigate(R.id.action_homeFragment_to_profileFragment)
                }
            }
        }
    }
}