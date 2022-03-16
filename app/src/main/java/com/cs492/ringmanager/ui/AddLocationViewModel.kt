package com.cs492.ringmanager.ui

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs492.ringmanager.api.MovieGluService
import com.cs492.ringmanager.data.LocationData
import com.cs492.ringmanager.data.LocationDatabase
import com.cs492.ringmanager.data.LocationRepository
import kotlinx.coroutines.launch

class AddLocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LocationRepository(LocationDatabase.getInstance(application).locationDao(), MovieGluService.create())

    fun loadLocation (
        location: LocationData
    ) {
        viewModelScope.launch {
            val result = repository.addLocation(location)
        }
    }

}