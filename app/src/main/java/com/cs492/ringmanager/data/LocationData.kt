package com.cs492.ringmanager.data

import androidx.room.Entity
import java.io.Serializable

@Entity(primaryKeys = ["latitude", "longitude"])
data class LocationData(
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lng") val longitude: Double,
    val radius: Float = 75,
    val name: String
) : Serializable