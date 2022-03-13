package com.cs492.ringmanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationWithRing)

    @Query("SELECT * FROM Locations")
    suspend fun getAllLocations(): List<LocationWithRing>


}