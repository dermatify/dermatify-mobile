package com.bangkit.android.dermatify.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.databinding.ItemJourneyBinding
import com.bangkit.android.dermatify.util.setUriToImageView

class JourneyAdapter(private val scans: List<Scans>) : ListAdapter<Scans, JourneyAdapter.JourneyViewHolder>(DIFF_CALLBACK) {

    inner class JourneyViewHolder(private val binding: ItemJourneyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvDiagnosis.text = scans[position].diagnosis
                tvDiagnosisDesc.text = scans[position].description
                tvScansResultDate.text = scans[position].timestamp
                val imageUri = Uri.parse(scans[position].imageUri)
                ivScans.setUriToImageView(imageUri)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JourneyViewHolder {
        return JourneyViewHolder(
            ItemJourneyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: JourneyAdapter.JourneyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = 2


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Scans> =
            object : DiffUtil.ItemCallback<Scans>() {
                override fun areItemsTheSame(oldItem: Scans, newItem: Scans): Boolean {
                    return oldItem.id == newItem.id
                }

                @Suppress("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: Scans, newItem: Scans): Boolean {
                    return oldItem == newItem
                }
            }
    }
}