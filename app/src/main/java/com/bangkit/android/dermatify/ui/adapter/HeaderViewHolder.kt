package com.bangkit.android.dermatify.ui.adapter

import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.ItemHeaderHomeBinding

sealed class HeaderViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class HomeHeaderViewHolder(
        private val binding: ItemHeaderHomeBinding,
        private val navController: NavController,
        private val context: Context,
        private var name: String,
        private var userPic: String) : HeaderViewHolder(binding) {
        fun bind() {
            binding.apply {
                btnAccProfile.setOnClickListener {
                    navController.navigate(R.id.action_homeFragment_to_profileFragment)
                }
                tvWelcomeHeader.text = context.getString(R.string.hi_name_home_header, name)

                if (userPic.isNotEmpty()) {
                    val pic = Uri.parse(userPic)
                    ivAccProfile.setImageURI(pic)
                }
            }
        }
    }
}