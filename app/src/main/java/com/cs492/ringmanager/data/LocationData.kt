package com.cs492.ringmanager.data

import androidx.room.Entity
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(primaryKeys = ["latitude", "longitude"])
data class LocationData(
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lng") val longitude: Double,
    val radius: Float = 75F,
    @Json(name = "cinemaName")val name: String = "Default"
) : Serializable

data class Cinemas(
    val cinemas: List<LocationData>
)