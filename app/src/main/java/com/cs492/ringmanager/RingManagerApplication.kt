package com.cs492.ringmanager

import android.app.Application
import androidx.work.*
import android.app.NotificationManager
import com.cs492.ringmanager.work.GeofenceWorker
import java.util.concurrent.TimeUnit

class RingManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        launchGeofenceWorker()
    }

    private fun launchGeofenceWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<GeofenceWorker>(
            15,
            TimeUnit.MINUTES
        ).setConstraints(constraints).build()
        //This is a hackish way to break the 15 minute interval limit.
//        val workRequest = OneTimeWorkRequestBuilder<GeofenceWorker>()
//            .setInitialDelay(5, TimeUnit.MINUTES)
        WorkManager.getInstance(this).enqueue(workRequest)
    }
}