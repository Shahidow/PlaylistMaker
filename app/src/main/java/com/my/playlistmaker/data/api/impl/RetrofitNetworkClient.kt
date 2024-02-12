package com.my.playlistmaker.data.api.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.my.playlistmaker.data.api.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private  val context: Context): NetworkClient {

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = -1 }
        if (dto !is TracksSearchRequest) return Response().apply { resultCode = 400 }
        val response = trackService.search(dto.expression).execute()
        val body = response.body()
        if (body != null) return body.apply { resultCode = response.code() }
        else return Response().apply { resultCode = response.code() }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}