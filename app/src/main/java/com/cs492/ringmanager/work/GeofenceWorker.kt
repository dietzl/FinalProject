package com.cs492.ringmanager.work

import android.content.BroadcastReceiver
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cs492.ringmanager.api.MovieGluService
import com.cs492.ringmanager.data.LocationRepository
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices

//Time in ms before the geofence expires. Currently setting this to 5 minutes past
const val GEOFENCE_DURATION = 1200000
//TODO: Is this the right duration? Should we instead manually remove all geofences before adding a fresh set?

class GeofenceWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params){

    lateinit var geofencingClient: GeofencingClient
    val locationRepo = LocationRepository(service = MovieGluService())

    override suspend fun doWork(): Result {
        geofencingClient = LocationServices.getGeofencingClient(applicationContext)

        val allLocations = locationRepo.getAllLocations()


        for(location in allLocations){
            Geofence.Builder()
                .setRequestId(location.latitude.toString()+","+location.longitude.toString())
                .setCircularRegion(
                    location.latitude,
                    location.longitude,
                    location.radius
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        }



        return Result.success()
    }
}