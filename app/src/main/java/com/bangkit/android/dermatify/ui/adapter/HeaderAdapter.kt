package com.bangkit.android.dermatify.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.databinding.ItemHeaderHomeBinding
import com.bangkit.android.dermatify.databinding.ItemLearnBotHeaderBinding
import com.bangkit.android.dermatify.databinding.ItemLearnTopHeaderBinding
import retrofit2.http.Header

class HeaderAdapter(
    private val tabType: String,
    private val navController: NavController? = null,
    private val context: Context,
    var name: String = "",
    var userPic: String = "",
    private val articles: List<ArticlesItem> = emptyList()
) : RecyclerView.Adapter<HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return when (tabType) {
            HOME -> {
                HeaderViewHolder.HomeHeaderViewHolder(
                    ItemHeaderHomeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    navController!!,
                    context,
                    name,
                    userPic
                )
            }
            LEARN_TOP -> {
                HeaderViewHolder.LearnTopHeaderViewHolder(
                    ItemLearnTopHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    context,
                    articles
                )
            }
            else -> {
                HeaderViewHolder.LearnBotHeaderViewHolder(
                    ItemLearnBotHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        if (holder is HeaderViewHolder.HomeHeaderViewHolder) {
            holder.bind()
        } else if (holder is HeaderViewHolder.LearnTopHeaderViewHolder) {
            holder.bind()
        }

    }


    override fun getItemCount(): Int = 1

    companion object {
        const val HOME = "HOME"
        const val LEARN_TOP = "LEARN_TOP"
        const val LEARN_BOT = "LEARN_BOT"
    }

}