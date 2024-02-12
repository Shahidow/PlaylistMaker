package com.my.playlistmaker.data.api.impl

import com.my.playlistmaker.Track
import com.my.playlistmaker.data.api.NetworkClient
import com.my.playlistmaker.data.api.TracksRepository
import com.my.playlistmaker.data.api.TracksSearchRequest
import com.my.playlistmaker.domain.api.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as TracksSearchResponse).results)
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}