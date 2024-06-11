package com.bangkit.android.dermatify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.databinding.ItemHeaderHomeBinding

class HeaderAdapter(private val tabType: String, private val navController: NavController? = null) : RecyclerView.Adapter<HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return if (tabType == HeaderAdapter.HOME) {
            HeaderViewHolder.HomeHeaderViewHolder(
                ItemHeaderHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                navController!!
            )

        } else {
            HeaderViewHolder.HomeHeaderViewHolder(
                ItemHeaderHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                navController!!
            )
        }
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        (holder as HeaderViewHolder.HomeHeaderViewHolder).bind()
    }

    override fun getItemCount(): Int = 1

    companion object {
        const val HOME = "HOME"
        const val LEARN_TOP = "LEARN_TOP"
        const val LEARN_BOT = "LEARN_BOT"
    }

}