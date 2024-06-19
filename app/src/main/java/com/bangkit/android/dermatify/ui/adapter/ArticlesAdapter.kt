package com.bangkit.android.dermatify.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.databinding.ItemArticlesBinding
import com.bangkit.android.dermatify.databinding.ItemArticlesLearnTopBinding
import com.bangkit.android.dermatify.databinding.ItemLearnTopHeaderBinding
import com.bangkit.android.dermatify.databinding.ItemRowLearnBinding

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
            LEARN_HIGHLIGHTS -> ArticlesViewHolder.LearnHighlightsArticlesViewHolder(
                ItemArticlesLearnTopBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                context = context,
                articles = articles
            )
            else -> ArticlesViewHolder.LearnBotArticlesViewHolder(
                ItemRowLearnBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                context = context,
                articles = articles
            )
        }
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        when (holder) {
            is ArticlesViewHolder.HighlightsArticlesViewHolder -> holder.bind(position)
            is ArticlesViewHolder.LearnHighlightsArticlesViewHolder -> holder.bind(position)
            is ArticlesViewHolder.LearnBotArticlesViewHolder -> holder.bind(position)
        }
    }

    override fun getItemCount(): Int {
        return when (tabType) {
            HIGHLIGHTS, LEARN_HIGHLIGHTS -> 4
            LEARN_BOT -> 9
            else -> 0
        }
    }


    companion object {
        const val HIGHLIGHTS = "HOME_HIGHLIGHTS"
        const val LEARN_HIGHLIGHTS = "LEARN_HIGHLIGHTS"
        const val LEARN_BOT = "LEARN_BOT"
    }
}
