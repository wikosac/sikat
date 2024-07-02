package com.rstj.sikat.src.model

import kotlinx.serialization.Serializable

@Serializable
data class DriverModel(
    val id: Int,
    val plat_angkot: String,
    val rute: String,
    val nama: String,
    val ttl: String,
    val usia: String,
    val jenis_kelamin: String,
    val alamat: String
)
