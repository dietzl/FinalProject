package com.cs492.ringmanager.data

import com.cs492.ringmanager.api.MovieGluService
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val dao: LocationDao,
    private val service: MovieGluService) {

    fun getAllLocations(): List<LocationData> {
        val allLocations = mutableListOf<LocationData>()
        allLocations.addAll(getUserLocationsOnce())
        allLocations.addAll(getServiceLocations())
        return allLocations
    }

    fun getUserLocations(): Flow<List<LocationData>> {
        return dao.getAllLocations()
    }

    fun getUserLocationsOnce(): List<LocationData> {
        return dao.getAllLocationsOnce()
    }

    fun getServiceLocations(): List<LocationData>{
        val serviceLocations = mutableListOf<LocationData>()
        TODO("Add caching logic. If we haven't moved beyond a certain distance then we should pull from the cache." +
                "If we have moved far enough away then dump the locations and call the movieglu api again." +
                "")
        return serviceLocations
    }
}