package com.rstj.sikat.src.maps

data class RouteModel(
    val stops: List<TransitModel>,
    val color: Int,
    val title: String
)
