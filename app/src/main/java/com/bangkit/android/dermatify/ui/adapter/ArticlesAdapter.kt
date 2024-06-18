package com.bangkit.android.dermatify.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.databinding.ItemArticlesBinding

class ArticlesAdapter(
    private val tabType: String,
    private val navController: NavController? = null,
    private val context: Context,
    private val articles: List<ArticlesItem> = emptyList()
) : RecyclerView.Adapter<ArticlesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return when (tabType) {
            HIGHLIGHTS -> ArticlesViewHolder.HighlightsArticlesViewHolder(
                ItemArticlesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                context = context,
                articles = articles
            )
            else -> ArticlesViewHolder.HighlightsArticlesViewHolder(
                ItemArticlesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                context = context
            )
        }
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        when (holder) {
            is ArticlesViewHolder.HighlightsArticlesViewHolder -> holder.bind(position)
        }
    }

    override fun getItemCount(): Int {
        return when (tabType) {
            HIGHLIGHTS -> 4
            LEARN_BOT -> 10
            else -> 0
        }
    }


    companion object {
        const val HIGHLIGHTS = "HOME_LEARN_HIGHLIGHTS"
        const val LEARN_BOT = "LEARN_BOT"
    }
}
