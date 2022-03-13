package com.cs492.ringmanager.data

import android.location.Location
import com.cs492.ringmanager.api.MovieGluService

class LocationRepository(private val service: MovieGluService) {
    fun getAllLocations(): List<LocationWithRing> {
        val allLocations = mutableListOf<LocationWithRing>()
        allLocations.addAll(getUserLocations())
        allLocations.addAll(getServiceLocations())
        return allLocations
    }

    fun getUserLocations(): List<LocationWithRing> {
        val userLocations = listOf<LocationWithRing>()
        TODO("Implement repository methods")
        return userLocations
    }

    fun getServiceLocations(): List<LocationWithRing>{
        val serviceLocations = mutableListOf<LocationWithRing>()
        TODO("Add caching logic. If we haven't moved beyond a certain distance then we should pull from the cache." +
                "If we have moved far enough away then dump the locations and call the movieglu api again.")
        return serviceLocations
    }
}