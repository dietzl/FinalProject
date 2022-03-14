package com.cs492.ringmanager.ui

import com.cs492.ringmanager.data.LocationDao
import com.cs492.ringmanager.data.LocationData


class BookmarkedLocations (private val dao: LocationDao) {
    suspend fun insertLocation(location: LocationData) = dao.insert(location)
    fun getAllLocations() = dao.getAllLocations()
}