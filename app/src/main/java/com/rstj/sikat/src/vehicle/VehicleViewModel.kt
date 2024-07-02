package com.rstj.sikat.src.vehicle

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rstj.sikat.R
import com.rstj.sikat.src.model.DriverModel
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File

class VehicleViewModel() : ViewModel() {

    private val _driver = MutableLiveData<List<DriverModel>>()
    val driver: LiveData<List<DriverModel>> = _driver

    fun getData(context: Context) {
        val inputStream = context.resources.openRawResource(R.raw.driver_data)
        val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
        val drivers: List<DriverModel> = Json.decodeFromString(jsonString)
        _driver.value = drivers
    }
}