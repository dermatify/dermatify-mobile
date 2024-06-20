package com.bangkit.android.dermatify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.databinding.ItemJourneyBinding
import com.bangkit.android.dermatify.util.loadImg

class JourneyAdapter(private val onClick: (Scans) -> Unit) : RecyclerView.Adapter<JourneyAdapter.JourneyViewHolder>() {

    private var listScans = ArrayList<Scans>()

    fun setData(listScans: List<Scans>) {
        this.listScans.clear()
        this.listScans.addAll(listScans)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JourneyAdapter.JourneyViewHolder {
        val binding = ItemJourneyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JourneyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JourneyAdapter.JourneyViewHolder, position: Int) {
        val scan = listScans[position]
        holder.bind(scan)
    }

    override fun getItemCount(): Int = listScans.size

    inner class JourneyViewHolder(private val binding: ItemJourneyBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(scan: Scans) {
                    binding.apply {
                        root.setOnClickListener {
                            onClick(scan)
                        }
                        ivScans.loadImg(scan.imageUri)
                        tvDiagnosis.text = scan.diagnosis
                        tvDiagnosisDesc.text = scan.description
                        tvScansResultDate.text = scan.timestamp
                    }
                }
            }
}