package com.rstj.sikat.src.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "polylines")
data class PolylineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originLat: Double,
    val originLng: Double,
    val destLat: Double,
    val destLng: Double,
    val polyline: String,
    val color: Int,
    val startAddress: String,
    val endAddress: String
)