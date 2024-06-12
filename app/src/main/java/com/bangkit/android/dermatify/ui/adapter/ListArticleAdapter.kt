/*package com.bangkit.android.dermatify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.R

class ListArticleAdapter(private val listArticle: ArrayList<Article>) : RecyclerView.Adapter<ListArticleAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_learn, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listArticle.size

    override fun onBindViewHolder(holder: ListArticleAdapter.ListViewHolder, position: Int) {
        val (title, subtitle, thumbnail) = ListAdapter[position]
        holder.ivLearnThumbnail.setImageResource(thumbnail)
        holder.tvLearnTitle.text = title
        holder.tvLearnSubtitle.text = subtitle
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listArticle[holder.adapterPosition])
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Article)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivLearnThumbnail: ImageView = itemView.findViewById(R.id.iv_learn_thumbnail)
        val tvLearnTitle: TextView = itemView.findViewById(R.id.tv_learn_title)
        val tvLearnSubtitle: TextView = itemView.findViewById(R.id.tv_learn_subtitle)
    }
}

 */
