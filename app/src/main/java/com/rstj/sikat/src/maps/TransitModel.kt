package com.rstj.sikat.src.maps

import com.google.android.gms.maps.model.LatLng

data class TransitModel(
    val coordinate: LatLng,
    val tag: String
)
