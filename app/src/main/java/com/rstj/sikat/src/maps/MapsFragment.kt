package com.rstj.sikat.src.maps

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.rstj.sikat.BuildConfig
import com.rstj.sikat.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        val rita = LatLng(-6.870550430170776, 109.11867025091709)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rita, 13f))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = false

        val terminal = LatLng(-6.8726878722179565, 109.10778548169424)
        val halteYosSudarso = LatLng(-6.854897, 109.136277)
        val haltePoltran = LatLng(-6.869972, 109.145281)
        val halteSmpMuh = LatLng(-6.872449, 109.137383)
        val halteMayjend = LatLng(-6.869306, 109.128994)
        val banjaran = LatLng(-6.941277827215492, 109.13639344087923)
        val halteSmp6 = LatLng(-6.859115, 109.125995)
        val slawi = LatLng(-6.983040804249951, 109.13758624187152)

        val allPositions = listOf(terminal, halteYosSudarso, haltePoltran, halteSmpMuh, halteMayjend, banjaran, halteSmp6, slawi)
        makeMarker(googleMap, allPositions, "Halte")

        val jalurA1 = listOf(terminal, halteYosSudarso, haltePoltran, terminal)
        val jalurA2 = listOf(terminal, halteSmpMuh, haltePoltran, halteMayjend, terminal)
        val jalurTegalBanjaran = listOf(terminal, halteSmp6, banjaran, halteMayjend, terminal)
        val jalurTegalSlawi = listOf(terminal, halteYosSudarso, halteSmpMuh, banjaran, slawi)
        makeRoute(jalurA1, googleMap, Color.CYAN)
        makeRoute(jalurA2, googleMap, Color.RED)
        makeRoute(jalurTegalBanjaran, googleMap, Color.MAGENTA)
        makeRoute(jalurTegalSlawi, googleMap, Color.GREEN)
    }

    private fun makeRoute(coordinates: List<LatLng>, googleMap: GoogleMap, color: Int) {
        for (i in 0 until coordinates.size - 2) {
            fetchRoute(coordinates[i], coordinates[i + 1], googleMap, color)
        }
    }

    private fun makeMarker(googleMap: GoogleMap, positions: List<LatLng>, title: String) {
        positions.forEach {
            googleMap.addMarker(MarkerOptions().position(it).title(title))
        }
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

    private fun fetchRoute(origin: LatLng, destination: LatLng, map: GoogleMap, color: Int) {
        val url = getDirectionsUrl(origin, destination)
        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            Log.d("MapsFragment", "fetching: $url")
            val responseData = response.body?.string()
            if (responseData != null) {
                val jsonObject = JSONObject(responseData)
                val routes = jsonObject.getJSONArray("routes")
                if (routes.length() > 0) {
                    val points = routes.getJSONObject(0)
                        .getJSONObject("overview_polyline")
                        .getString("points")
                    val polylineOptions = decodePolyline(points, color)
                    CoroutineScope(Dispatchers.Main).launch {
                        map.addPolyline(polylineOptions)
                    }
                }
            }
        }
    }

    private fun getDirectionsUrl(origin: LatLng, destination: LatLng): String {
        val strOrigin = "origin=${origin.latitude},${origin.longitude}"
        val strDest = "destination=${destination.latitude},${destination.longitude}"
        val key = "key=${BuildConfig.MAPS_API_KEY}"
        return "https://maps.googleapis.com/maps/api/directions/json" +
                "?$strOrigin&$strDest&$key"
    }

    private fun decodePolyline(encoded: String, color: Int): PolylineOptions {
        val polylineOptions = PolylineOptions().color(color).width(4f)
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                (lat.toDouble() / 1E5),
                (lng.toDouble() / 1E5)
            )
            polylineOptions.add(p)
        }
        return polylineOptions
    }
}