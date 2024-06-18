package com.bangkit.android.dermatify.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.databinding.ItemRowLearnBinding
import com.bangkit.android.dermatify.util.loadImg
import com.bumptech.glide.Glide

class ListArticleAdapter(private val tabType: String, val newList: List<ArticlesItem>? = null) : RecyclerView.Adapter<ListArticleAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ItemRowLearnBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (newList != null) {
                binding.apply {
                    cardView.setOnClickListener {
                        val webpage: Uri = Uri.parse(newList[position].newsUrl)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        cardView.context.startActivity(intent)
                    }
                    ivLearnThumbnail.loadImg(newList[position].newsUrl!!)
                    tvLearnTitle.text = newList[position].title!!.trim()
                    tvLearnSubtitle.text = newList[position].subtitle!!.trim()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemRowLearnBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return when (tabType) {
            LISTLEARN -> 10
            else -> 0
        }
    }

    companion object {
        const val LISTLEARN = "LIST_LEARN"
    }
}

