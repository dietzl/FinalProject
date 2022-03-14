package com.cs492.ringmanager.data

import androidx.room.Entity
import java.io.Serializable

@Entity(primaryKeys = ["latitude", "longitude"])
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val ringerMode: Int //This is the value of the AudioManager.RINGER_MODE enumeration
) : Serializable