package com.cs492.ringmanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cs492.ringmanager.data.LocationDatabase
import com.cs492.ringmanager.data.LocationData
import kotlinx.coroutines.launch

class BookmarkedLocationsViewModel(application: Application) : AndroidViewModel(application){
    private val locations = BookmarkedLocations(
        LocationDatabase.getInstance(application).locationDao()
    )

    val savedLocations = locations.getAllLocations().asLiveData()

    fun addLocation(location: LocationData) {
        viewModelScope.launch {
            locations.insertLocation(location)
        }
    }

    fun getAllLocations() {
        viewModelScope.launch {
            locations.getAllLocations()
        }
    }
}