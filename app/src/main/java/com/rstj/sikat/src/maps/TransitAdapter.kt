package com.rstj.sikat.src.maps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rstj.sikat.databinding.ItemTransitBinding

class TransitAdapter(
    private val transitList: List<TransitModel>,
    private val listener: OnTransitItemClickListener
): RecyclerView.Adapter<TransitAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemTransitBinding): RecyclerView.ViewHolder(binding.root) {

        private val tvTransitName = binding.tvTransitName

        fun bind(tag: String) {
            tvTransitName.text = tag
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTransitItemClick(transitList[position])
                }
            }
        }
    }

    interface OnTransitItemClickListener {
        fun onTransitItemClick(transitModel: TransitModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTransitBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return transitList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transitList[position].tag)
    }
}