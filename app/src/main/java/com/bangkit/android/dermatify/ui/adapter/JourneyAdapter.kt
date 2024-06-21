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

class JourneyAdapter : ListAdapter<Scans, JourneyAdapter.JourneyViewHolder>(DIFF_CALLBACK) {

    private var scans: MutableList<Scans> = mutableListOf()

    inner class JourneyViewHolder(private val binding: ItemJourneyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scans: Scans) {
            binding.apply {
                tvDiagnosis.text = scans.diagnosis
                tvDiagnosisDesc.text = scans.description
                tvScansResultDate.text = scans.timestamp
                val imageUri = Uri.parse(scans.imageUri)
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
        holder.bind(scans[position])
    }

    override fun getItemCount(): Int = scans.size

    fun setScans(scansList: List<Scans>) {
        this.scans.clear()
        this.scans.addAll(scansList)
        notifyDataSetChanged()
    }


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