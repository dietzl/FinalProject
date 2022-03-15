package com.cs492.ringmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationData)

    @Delete
    suspend fun delete(location: LocationData)

    @Query("SELECT * FROM LocationData")
    fun getAllLocations(): Flow<List<LocationData>>

    @Query("SELECT * FROM LocationData")
    suspend fun getAllLocationsOnce(): List<LocationData>

}