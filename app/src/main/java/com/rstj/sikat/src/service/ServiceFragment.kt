package com.rstj.sikat.src.service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rstj.sikat.R
import com.rstj.sikat.databinding.FragmentServiceBinding
import com.rstj.sikat.src.model.DriverModel
import com.rstj.sikat.src.vehicle.VehicleAdapter
import kotlinx.serialization.json.Json
import java.io.BufferedReader

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setItemData(getData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        val adapter = VehicleAdapter(driver, angkotImages, requireContext())
        binding.rvDriver.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getData(): List<DriverModel> {
        val inputStream = requireContext().resources.openRawResource(R.raw.driver_data)
        val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
        val drivers: List<DriverModel> = Json.decodeFromString(jsonString)
        Log.d("ServiceFragment", "getData: $drivers")
        return drivers
    }
}