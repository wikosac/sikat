package com.rstj.sikat.src.vehicle

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rstj.sikat.R
import com.rstj.sikat.databinding.ItemVehicleBinding
import com.rstj.sikat.src.model.DriverModel

class VehicleAdapter(
    private val driverModel: List<DriverModel>,
    private val imgResId: List<Int>,
    private val context: Context,
): RecyclerView.Adapter<VehicleAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imgAngkot = binding.imgAngkot
        private val tvPlat = binding.tvPlat
        private val tvNameDriver = binding.tvNameDriver
        private val tvRoute = binding.tvRoute



        fun bind(driverModel: DriverModel, imgResId: Int, context: Context) {
            imgAngkot.setImageResource(imgResId)
            tvPlat.text = context.getString(R.string.plat_nomor, driverModel.plat_angkot)
            tvNameDriver.text = context.getString(R.string.nama_pengemudi, driverModel.nama)
            tvRoute.text = context.getString(R.string.rute, driverModel.rute)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVehicleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(driverModel[position], imgResId[position], context)
    }

    override fun getItemCount(): Int {
        return driverModel.size
    }
}