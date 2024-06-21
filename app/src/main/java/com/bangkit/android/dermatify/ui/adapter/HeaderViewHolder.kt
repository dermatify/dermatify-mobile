package com.bangkit.android.dermatify.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Highlights
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.databinding.ItemArticlesBinding
import com.bangkit.android.dermatify.databinding.ItemHeaderHomeBinding
import com.bangkit.android.dermatify.databinding.ItemLearnBotHeaderBinding
import com.bangkit.android.dermatify.databinding.ItemLearnTopHeaderBinding
import com.bangkit.android.dermatify.ui.home.HomeFragmentDirections
import com.bangkit.android.dermatify.util.setUriToImageView
import com.bangkit.android.dermatify.util.visible

sealed class HeaderViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class HomeHeaderViewHolder(
        private val binding: ItemHeaderHomeBinding,
        private val navController: NavController,
        private val context: Context,
        private var name: String,
        private val scans: Scans? = null) : HeaderViewHolder(binding) {
        fun bind() {
            binding.apply {
                if (scans != null) {
                    cvRecentScans.visible()
                    tvRecentScans.visible()
                    btnAllResults.visible()
                    val picUri = Uri.parse(scans.imageUri)
                    ivScans.setUriToImageView(picUri)
                    tvDiagnosisDesc.text = scans.description
                    tvScansResultDate.text = scans.timestamp
                    tvDiagnosis.text = scans.diagnosis
                    when (scans.diagnosis) {
                        "Acne" -> {
                            ivDiagnosis.setImageResource(R.drawable.ic_acnes)
                        }
                        "Eyebags" -> {
                            ivDiagnosis.setImageResource(R.drawable.ic_dark_circle)
                        }
                        "Redness" -> {
                            ivDiagnosis.setImageResource(R.drawable.ic_redness)
                        }
                    }
                    cvRecentScans.setOnClickListener {
                        navController.navigate(HomeFragmentDirections.actionHomeFragmentToExamineResultFragment(
                            diagnosis = scans.diagnosis,
                            createdAt = scans.timestamp,
                            id = scans.id.toString(),
                            picUri = scans.imageUri
                        ))
                    }
                }


                tvWelcomeHeader.text = context.getString(R.string.hi_name_home_header, name)

                btnAllResults.setOnClickListener {
                    navController.popBackStack(R.id.main_nav, true)
                    navController.navigate(R.id.journeyFragment)
                }

                btnLearnMore.setOnClickListener {
                    navController.popBackStack(R.id.main_nav, true)
                    navController.navigate(R.id.learnFragment)
                }

                btnCtaToExamine.setOnClickListener {
                    navController.navigate(R.id.action_homeFragment_to_examineFragment)
                }
            }
        }
    }

    class LearnTopHeaderViewHolder(
        private val binding: ItemLearnTopHeaderBinding,
        private val context: Context,
        private val articles: List<ArticlesItem> = emptyList()
    ) : HeaderViewHolder(binding) {
        fun bind() {
            binding.apply {
                rvArticles.adapter = ArticlesAdapter(ArticlesAdapter.LEARN_HIGHLIGHTS, context = context, articles = articles)
                rvArticles.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            }
        }
    }

    class LearnBotHeaderViewHolder(private val binding: ItemLearnBotHeaderBinding) : HeaderViewHolder(binding) {

    }
}