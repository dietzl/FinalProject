package com.cs492.ringmanager.api

import com.cs492.ringmanager.data.LocationWithRing
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MovieGluService {
    //https://developer.movieglu.com/v2/api-index/cinemasNearby/
    @GET("cinemasNearby")
    fun getCinemasNearby(location: LocationWithRing){

    }
}