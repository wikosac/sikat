package com.rstj.sikat.src.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rstj.sikat.R

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        val tegal = LatLng(-6.867256703895116, 109.13782692188727)
        googleMap.addMarker(MarkerOptions().position(tegal).title("Marker in Tegal"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tegal, 12f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        view.findViewById<CardView>(R.id.btn_back).setOnClickListener { requireActivity().finish() }
    }
}