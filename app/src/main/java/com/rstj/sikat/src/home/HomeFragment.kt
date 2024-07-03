package com.rstj.sikat.src.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rstj.sikat.databinding.FragmentHomeBinding
import com.rstj.sikat.src.maps.MapsActivity
import com.rstj.sikat.src.vehicle.VehicleActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardTrayek.setOnClickListener {
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }
        binding.cardVehicle.setOnClickListener {
            startActivity(Intent(requireActivity(), VehicleActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}