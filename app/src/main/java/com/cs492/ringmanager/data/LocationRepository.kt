package com.cs492.ringmanager.data

import com.cs492.ringmanager.api.MovieGluService
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val dao: LocationDao,
    private val service: MovieGluService) {

    suspend fun getAllLocations(): List<LocationData> {
        val allLocations = mutableListOf<LocationData>()
        allLocations.addAll(getUserLocationsOnce())
        allLocations.addAll(getServiceLocations())
        return allLocations
    }

    fun getUserLocations(): Flow<List<LocationData>> = dao.getAllLocations()
    suspend fun getUserLocationsOnce(): List<LocationData> = dao.getAllLocationsOnce()
    suspend fun addLocation(location: LocationData) = dao.insert(location)
    suspend fun removeUserLocation(location: LocationData) = dao.delete(location)


    fun getServiceLocations(): List<LocationData>{
        val serviceLocations = mutableListOf<LocationData>()
        TODO("Add caching logic. If we haven't moved beyond a certain distance then we should pull from the cache." +
                "If we have moved far enough away then dump the locations and call the movieglu api again." +
                "")
        return serviceLocations
    }

}