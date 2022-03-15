package com.cs492.ringmanager.api

import com.cs492.ringmanager.data.LocationData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Headers

//https://developer.movieglu.com/v2/api-index/cinemasNearby/
//https://api-gate2.movieglu.com/cinemasNearby/?n=5
interface MovieGluService {
    @Headers({
        "client: OREG ",
        "x-api-key: x18jFgFa0n7sxB0k2QxR15YlOAeo5OUo8cJyhMnj ",
        "authorization: Basic T1JFR19YWDpCWm9ub1d5UDJFMUw= ",
        "territory: XX ",
        "api-version: v200 "})
    @GET("cinemasNearby")
    suspend fun getCinemasNearby(
        @Header("Geolocation") geolocation: String,
        @Header("Device-datetime") device-datetime: String,
        @Query("n") maxResults: Int = 25):ListOf<LocationWithRing>

    companion object{
        private const val BASE_URL = "https://api-gate2.movieglu.com"
        fun create() : MovieGluService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MovieGluService::class.java)
        }
    }
}