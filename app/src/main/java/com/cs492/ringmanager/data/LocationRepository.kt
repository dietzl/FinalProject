package com.cs492.ringmanager.data

import com.cs492.ringmanager.api.MovieGluService
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val dao: LocationDao,
    private val service: MovieGluService) {

    suspend fun getAllLocations(location: LocationData): List<LocationData> {
        val allLocations = mutableListOf<LocationData>()
        allLocations.addAll(getUserLocationsOnce())
        allLocations.addAll(getServiceLocations(location))
        return allLocations
    }

    fun getUserLocations(): Flow<List<LocationData>> = dao.getAllLocations()
    suspend fun getUserLocationsOnce(): List<LocationData> = dao.getAllLocationsOnce()
    suspend fun addLocation(location: LocationData) = dao.insert(location)
    suspend fun removeUserLocation(location: LocationData) = dao.delete(location)

    suspend fun getServiceLocations(location: LocationData): List<LocationData>{
        val serviceLocations = mutableListOf<LocationData>()
        service.getCinemasNearbyByLocation(location)
        return serviceLocations
    }

}