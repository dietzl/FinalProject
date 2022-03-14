package com.cs492.ringmanager.api

import com.cs492.ringmanager.data.LocationData
import retrofit2.http.GET

class MovieGluService {
    //https://developer.movieglu.com/v2/api-index/cinemasNearby/
    @GET("cinemasNearby")
    fun getCinemasNearby(location: LocationData){

    }
}