package com.rstj.sikat.src.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rstj.sikat.databinding.ItemHistoryBinding

class HistoryAdapter(private val historyModel: List<HistoryModel>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyModel: HistoryModel) {
            binding.tvTitleRoute.text = historyModel.title
            binding.tvDateRoute.text = historyModel.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return historyModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyModel[position])
    }

}
