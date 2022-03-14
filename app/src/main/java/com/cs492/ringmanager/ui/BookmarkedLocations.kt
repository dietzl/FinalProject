package com.cs492.ringmanager.ui

import com.cs492.ringmanager.data.LocationDao
import com.cs492.ringmanager.data.LocationWithRing


class BookmarkedLocations (private val dao: LocationDao) {
    suspend fun insertLocation(location: LocationWithRing) = dao.insert(location)
    fun getAllLocations() = dao.getAllLocations()
}