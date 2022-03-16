package com.cs492.ringmanager.ui

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import com.cs492.ringmanager.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.cs492.ringmanager.api.MovieGluService
import com.cs492.ringmanager.data.LocationDao
import com.cs492.ringmanager.data.LocationData
import com.cs492.ringmanager.data.LocationDatabase
import com.cs492.ringmanager.data.LocationRepository
import com.cs492.ringmanager.work.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
private const val RECACHE_DISTANCE_IN_METERS = 5000F

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    val movieGluService = MovieGluService.create()
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var dao: LocationDao
    private lateinit var locationRepo: LocationRepository

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = SILENCE_GEOFENCE_EVENT
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val recachePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = RECACHE_GEOFENCE_EVENT
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dao = LocationDatabase.getInstance(this).locationDao()
        locationRepo = LocationRepository(dao, movieGluService)
        setupFragments()
        geofencingClient = LocationServices.getGeofencingClient(this)

        setupTheaterGeofences()
        setupRecacheGeofence()
    }

    @SuppressLint("MissingPermission")
    fun setupTheaterGeofences() {
        lifecycleScope.launch {
            val locationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
            //Get current location because we're going to find theater locations nearby
            lateinit var currentLocation: LocationData
            locationClient.lastLocation.addOnSuccessListener { location ->
                currentLocation =
                    LocationData(location.latitude, location.longitude, 75F, "CurrentLocation")
            }
            //Get both user locations and locations returned by movieglu
            val allLocations = locationRepo.getAllLocations(currentLocation)
            //Make geofences for all locations
            val geofenceList = mutableListOf<Geofence>()
            for (location in allLocations) {
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
                    geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                        ?.run {
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
    }

    /**
     * Sets up a geofence that will trigger when the user leaves an area
     * to notify the app to update the cache of theater locations in the new area
     */
    @SuppressLint("MissingPermission")
    fun setupRecacheGeofence() {
        val locationClient = LocationServices.getFusedLocationProviderClient(this)

        locationClient.lastLocation.addOnSuccessListener { location ->
            val geoFence = Geofence.Builder()
                .setRequestId(RECACHE_GEOFENCE_KEY)
                .setCircularRegion(
                    location.latitude,
                    location.longitude,
                    RECACHE_DISTANCE_IN_METERS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()

            val geofencingRequest = GeofencingRequest.Builder()
                .addGeofence(geoFence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .build()
            //Remove any existing geofences of this type before we add a new one
            geofencingClient.removeGeofences(recachePendingIntent)?.run {
                addOnCompleteListener {
                    geofencingClient.addGeofences(geofencingRequest, recachePendingIntent)?.run {
                        addOnSuccessListener {
                            Log.i(TAG, "Recache geo fence added to geofencing client.")
                        }
                        addOnFailureListener {
                            Log.e(TAG, "Failed to add recaching geo fence to Geofencing Client.")
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupFragments() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    companion object {
        internal const val SILENCE_GEOFENCE_EVENT =
            "RingManager.geoFence.SILENCE_GEOFENCE_EVENT"
        internal const val RECACHE_GEOFENCE_EVENT =
            "RingManager.geoFence.RECACHE_GEOFENCE_EVENT"
        internal const val RECACHE_GEOFENCE_KEY =
            "RingManager.geoFence.RECACHE_GEOFENCE"
    }
}