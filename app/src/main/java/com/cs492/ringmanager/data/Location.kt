package com.cs492.ringmanager.data

import androidx.room.Entity

@Entity(primaryKeys = ["latitude", "longitude"])
data class Location(
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val ringerMode: Int //This is the value of the AudioManager.RINGER_MODE enumeration
)