package com.bangkit.android.dermatify.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.databinding.ItemArticlesBinding
import com.bangkit.android.dermatify.util.gone
import com.bangkit.android.dermatify.util.goneShimmer
import com.bangkit.android.dermatify.util.showShimmer
import com.bangkit.android.dermatify.util.visible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

sealed class ArticlesViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class HighlightsArticlesViewHolder(
        private val binding: ItemArticlesBinding,
        private val context: Context,
        private val articles: List<ArticlesItem> = emptyList()
    ) : ArticlesViewHolder(binding) {
        fun bind(position: Int) {
            binding.apply {
                shimmerLayout.showShimmer()

                if (articles.isNotEmpty()) {
                    cvArticles.visible()
                    tvArticleTitle.text = articles[position].title
                    tvArticleSubtitle.text = articles[position].subtitle
                    Log.d("Cilukba", "lewat art")
                    Glide.with(context)
                        .load(articles[position].thumbnail)
                        .error(
                            Glide.with(context)
                                .load(articles[position].thumbnail)
                        )
                        .centerCrop()
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.d("Cilukba", "lewat failed")
                                shimmerLayout.showShimmer()
                                return false
                            }
                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.d("Cilukba", "lewat ready")
                                shimmerLayout.goneShimmer()
                                return false
                            }
                        })
                        .into(ivArticlesThumbnail)
                }

                cvArticles.setOnClickListener {
                    val webpage: Uri = Uri.parse(articles[position].newsUrl)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    cvArticles.context.startActivity(intent)
                }
            }
        }
    }
}