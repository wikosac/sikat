package com.rstj.sikat.src.maps

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.rstj.sikat.BuildConfig
import com.rstj.sikat.R
import com.rstj.sikat.databinding.FragmentMapsBinding
import com.rstj.sikat.src.database.PolylineDao
import com.rstj.sikat.src.database.PolylineDatabase
import com.rstj.sikat.src.database.PolylineEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MapsFragment : Fragment(), TransitAdapter.OnTransitItemClickListener {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var polylineDatabase: PolylineDatabase
    private lateinit var polylineDao: PolylineDao

    private val routeList = mutableListOf<RouteModel>()
    private val markerList = mutableListOf<Marker>()
    private val callback = OnMapReadyCallback { maps ->
        googleMap = maps

        val rita = LatLng(-6.870550430170776, 109.11867025091709)
        maps.moveCamera(CameraUpdateFactory.newLatLngZoom(rita, 13f))
        maps.uiSettings.isZoomControlsEnabled = true
        maps.uiSettings.isRotateGesturesEnabled = false

        drawRoute()
        setupSpinner(routeList.map { it.title })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        polylineDatabase = PolylineDatabase.getDatabase(requireContext())
        polylineDao = polylineDatabase.polylineDao()

        binding.btnBack.setOnClickListener { requireActivity().finish() }
    }

    private fun zoomToFitMarkers() {
        if (markerList.isNotEmpty()) {
            val builder = LatLngBounds.Builder()
            markerList.forEach { marker ->
                builder.include(marker.position)
            }
            val bounds = builder.build()
            val padding = 200 // Padding from edges of the screen (in pixels)
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap.animateCamera(cu)
        } else {
            val rita = LatLng(-6.870550430170776, 109.11867025091709)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rita, 13f))
        }
    }

    private fun setupSpinner(routeTitles: List<String>) {
        // Create an ArrayAdapter using the string list and a default spinner layout
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, routeTitles)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.spinner.adapter = adapter

        // Optionally, set an item selected listener
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Do something with the selected item
                val selectedItem = parent.getItemAtPosition(position).toString()

                if (position == 0) {
                    googleMap.clear()
                    drawRoute()
                    markerList.clear()
                } else {
                    routeList.forEach { route ->
                        val transits = route.stops
                        val tags = transits.map { it.tag }
                        val coordinates = transits.map { it.coordinate }
                        if (route.title == selectedItem) {
                            googleMap.clear()
                            makeRoute(route)
                            showBottomSheet(route.title, transits)
                            makeMarker(coordinates, tags)
                        }
                    }
                }

                zoomToFitMarkers()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    private fun showBottomSheet(routeTitle: String, transitsTitle: List<TransitModel>) {
        // Check if the BottomSheetFragment is already shown
        val existingFragment =
            requireActivity().supportFragmentManager.findFragmentByTag(BottomSheetFragment.TAG)
        if (existingFragment == null) {
            BottomSheetFragment.newInstance(routeTitle, transitsTitle, this)
                ?.show(requireActivity().supportFragmentManager, BottomSheetFragment.TAG)
        }
    }

    private fun drawRoute() {
        val terminal = LatLng(-6.8726878722179565, 109.10778548169424)
        val halteYosSudarso = LatLng(-6.854897, 109.136277)
        val haltePoltran = LatLng(-6.869972, 109.145281)
        val halteSmpMuh = LatLng(-6.872449, 109.137383)
        val halteMayjend = LatLng(-6.869306, 109.128994)
        val banjaran = LatLng(-6.941277827215492, 109.13639344087923)
        val halteSmp6 = LatLng(-6.859115, 109.125995)
        val slawi = LatLng(-6.983040804249951, 109.13758624187152)

        val transitTerminal = TransitModel(terminal, "Terminal Tegal")
        val transitHalteYosSudarso = TransitModel(halteYosSudarso, "Halte Yos Sudarso")
        val transitHaltePoltran = TransitModel(haltePoltran, "Halte Poltran")
        val transitHalteSmpMuh = TransitModel(halteSmpMuh, "Halte SMP Muh")
        val transitHalteMayjend = TransitModel(halteMayjend, "Halte Mayjend")
        val transitBanjaran = TransitModel(banjaran, "Banjaran")
        val transitHalteSmp6 = TransitModel(halteSmp6, "Halte SMP 6")
        val transitSlawi = TransitModel(slawi, "Slawi")

        val jalurA1Transit =
            listOf(transitTerminal, transitHalteYosSudarso, transitHaltePoltran, transitTerminal)
        val jalurA2Transit = listOf(
            transitTerminal,
            transitHalteSmpMuh,
            transitHaltePoltran,
            transitHalteMayjend,
            transitTerminal
        )
        val jalurTegalBanjaranTransit = listOf(
            transitTerminal,
            transitHalteSmp6,
            transitBanjaran,
            transitHalteMayjend,
            transitTerminal
        )
        val jalurTegalSlawiTransit = listOf(
            transitTerminal,
            transitHalteYosSudarso,
            transitHalteSmpMuh,
            transitBanjaran,
            transitSlawi
        )

        val route0 = RouteModel(jalurA1Transit, Color.BLACK, "Pilih Rute")
        val route1 = RouteModel(jalurA1Transit, Color.CYAN, "Jalur A1")
        val route2 = RouteModel(jalurA2Transit, Color.RED, "Jalur A2")
        val route3 = RouteModel(jalurTegalBanjaranTransit, Color.MAGENTA, "Jalur Tegal Banjaran")
        val route4 = RouteModel(jalurTegalSlawiTransit, Color.GREEN, "Jalur Tegal Slawi")

        routeList.add(route0)
        routeList.add(route1)
        routeList.add(route2)
        routeList.add(route3)
        routeList.add(route4)

        makeRoute(route1)
        makeRoute(route2)
        makeRoute(route3)
        makeRoute(route4)
    }

    private fun makeRoute(route: RouteModel) {
        for (i in 0 until route.stops.size - 2) {
            fetchRoute(route.stops[i].coordinate, route.stops[i + 1].coordinate, route.color)
        }
    }

    private fun makeMarker(positions: List<LatLng>, titles: List<String>) {
        positions.forEach { latLng ->
            val marker = googleMap.addMarker(
                MarkerOptions().position(latLng).title(titles[positions.indexOf(latLng)])
            )
            marker?.let { markerList.add(it) }
        }
    }

    private fun fetchRoute(origin: LatLng, destination: LatLng, color: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val cachedPolyline = polylineDao.getPolyline(
                origin.latitude,
                origin.longitude,
                destination.latitude,
                destination.longitude
            )
            if (cachedPolyline != null) {
                // Decode the cached polyline and add it to the map
                val polylineOptions = decodePolyline(cachedPolyline.polyline, cachedPolyline.color)
                withContext(Dispatchers.Main) {
                    googleMap.addPolyline(polylineOptions)
                }
            } else {
                // Fetch the route from the API
                val url = getDirectionsUrl(origin, destination)
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

                        // Stops
                        val startAddress = routes.getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getString("start_address")
                        val endAddress = routes.getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getString("end_address")

                        // Cache the fetched polyline
                        val polylineEntity = PolylineEntity(
                            originLat = origin.latitude,
                            originLng = origin.longitude,
                            destLat = destination.latitude,
                            destLng = destination.longitude,
                            polyline = points,
                            color = color,
                            startAddress = startAddress,
                            endAddress = endAddress
                        )
                        polylineDao.insertPolyline(polylineEntity)

                        withContext(Dispatchers.Main) {
                            googleMap.addPolyline(polylineOptions)
                        }
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
        val polylineOptions = PolylineOptions().color(color).width(4f).clickable(true)
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

    override fun onTransitItemClick(transitModel: TransitModel) {
        markerList.forEach {
            if (it.position == transitModel.coordinate) {
                it.showInfoWindow()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, 14f))
            }
        }
    }
}
