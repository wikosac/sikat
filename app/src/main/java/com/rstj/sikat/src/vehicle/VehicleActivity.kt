package com.rstj.sikat.src.vehicle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rstj.sikat.R
import com.rstj.sikat.databinding.ActivityVehicleBinding
import com.rstj.sikat.src.model.DriverModel
import kotlinx.serialization.json.Json
import java.io.BufferedReader

class VehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setItemData(getData())

        binding.arrowBack.setOnClickListener { finish() }
    }

    private fun setItemData(driver: List<DriverModel>) {
        val angkotImages = listOf(
            R.drawable.angkot1,
            R.drawable.angkot2,
            R.drawable.angkot3,
            R.drawable.angkot4,
            R.drawable.angkot5,
            R.drawable.angkot6,
            R.drawable.angkot7,
            R.drawable.angkot8,
            R.drawable.angkot9,
            R.drawable.angkot10
        )

        val adapter = VehicleAdapter(driver, angkotImages, this)
        binding.rvVehicle.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@VehicleActivity)
        }
    }

    private fun getData(): List<DriverModel> {
        val inputStream = resources.openRawResource(R.raw.driver_data)
        val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
        val drivers: List<DriverModel> = Json.decodeFromString(jsonString)
        return drivers
    }
}