package com.cs492.ringmanager.data

import com.cs492.ringmanager.api.MovieGluService
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_INSTANT

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
        val currentTime =DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val cinemas = service.getCinemasNearby(
            location.latitude.toString()+";"+location.longitude,
            currentTime
        )
        return cinemas.cinemas
    }
}