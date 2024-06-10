package com.bangkit.android.dermatify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.databinding.ItemArticlesBinding

class ArticlesAdapter(private val tabType: String) : RecyclerView.Adapter<ArticlesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return when (tabType) {
            HIGHLIGHTS -> ArticlesViewHolder.HighlightsArticlesViewHolder(
                ItemArticlesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ArticlesViewHolder.HighlightsArticlesViewHolder(
                ItemArticlesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
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
            HIGHLIGHTS -> 5
            LEARN_BOT -> 10
            else -> 0
        }
    }


    companion object {
        const val HIGHLIGHTS = "HOME_LEARN_HIGHLIGHTS"
        const val LEARN_BOT = "LEARN_BOT"
    }
}
