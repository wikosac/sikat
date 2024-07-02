package com.rstj.sikat.src.vehicle

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rstj.sikat.databinding.ActivityVehicleBinding
import com.rstj.sikat.src.model.DriverModel

class VehicleActivity : AppCompatActivity() {

    private val binding: ActivityVehicleBinding by viewBinding()
    private val viewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}