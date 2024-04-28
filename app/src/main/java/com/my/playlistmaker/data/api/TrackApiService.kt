package com.my.playlistmaker.data.api

import com.my.playlistmaker.data.api.impl.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApiService {
    @GET("/search")
    suspend fun search(@Query("term") text: String): TracksSearchResponse
}