package com.rstj.sikat.src.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PolylineDao {
    @Query("SELECT * FROM polylines WHERE originLat = :originLat AND originLng = :originLng AND destLat = :destLat AND destLng = :destLng")
    suspend fun getPolyline(originLat: Double, originLng: Double, destLat: Double, destLng: Double): PolylineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolyline(polyline: PolylineEntity)
}