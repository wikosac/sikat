package com.rstj.sikat.src.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PolylineEntity::class], version = 1)
abstract class PolylineDatabase : RoomDatabase() {
    abstract fun polylineDao(): PolylineDao

    companion object {
        @Volatile
        private var INSTANCE: PolylineDatabase? = null

        fun getDatabase(context: Context): PolylineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PolylineDatabase::class.java,
                    "polyline_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
