package com.cs492.ringmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationWithRing)

    @Query("SELECT * FROM LocationWithRing")
    fun getAllLocations(): Flow<List<LocationWithRing>>


}