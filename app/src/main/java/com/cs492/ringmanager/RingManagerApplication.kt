package com.cs492.ringmanager

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.cs492.ringmanager.api.MovieGluService
import com.cs492.ringmanager.data.LocationDatabase
import com.cs492.ringmanager.data.LocationRepository
import com.cs492.ringmanager.work.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

private const val TAG = "MainApplication"

class RingManagerApplication : Application() {
    private lateinit var geofencingClient: GeofencingClient
    private val dao = LocationDatabase.getInstance(this).locationDao()
    val locationRepo = LocationRepository(dao, service = MovieGluService())

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = SILENCE_GEOFENCE_EVENT
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate() {
        super.onCreate()
        geofencingClient = LocationServices.getGeofencingClient(this)
        setupGeofence()
    }

    @SuppressLint("MissingPermission")
    private fun setupGeofence() {
        val allLocations = locationRepo.getAllLocationsOnce()
        val geofenceList = mutableListOf<Geofence>()

        for(location in allLocations) {
            geofenceList.add(
                Geofence.Builder()
                    .setRequestId(location.latitude.toString() + "," + location.longitude.toString())
                    .setCircularRegion(
                        location.latitude,
                        location.longitude,
                        location.radius
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build()
            )
        }
        val geofencingRequest = GeofencingRequest.Builder()
            .addGeofences(geofenceList)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
                    addOnSuccessListener {
                        Log.i(TAG, "Geo fences added to Geofencing Client.")
                    }
                    addOnFailureListener {
                        Log.e(TAG, "Failed to add geo fences to Geofencing Client.")
                    }
                }
            }
        }
    }

    companion object {
        internal const val SILENCE_GEOFENCE_EVENT =
            "RingManager.geoFence.ACTION_GEOFENCE_EVENT"
    }
}