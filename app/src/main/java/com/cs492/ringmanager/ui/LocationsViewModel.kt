package com.cs492.ringmanager.ui

import android.app.Application
import androidx.lifecycle.*
import com.cs492.ringmanager.api.MovieGluService
import com.cs492.ringmanager.data.LocationDatabase
import com.cs492.ringmanager.data.LocationData
import com.cs492.ringmanager.data.LocationRepository
import kotlinx.coroutines.launch

class LocationsViewModel(application: Application) : AndroidViewModel(application){

    private val repository = LocationRepository(
        LocationDatabase.getInstance(application).locationDao(),
        MovieGluService.create()
    )

    val savedLocations = repository.getUserLocations().asLiveData()

    fun addLocation(location: LocationData) {
        viewModelScope.launch {
            repository.addLocation(location)
        }
    }

    fun removeLocation(location: LocationData) {
        viewModelScope.launch {
            repository.removeUserLocation(location)
        }
    }
}