package com.my.playlistmaker.data.api.impl

import com.my.playlistmaker.Track
import com.my.playlistmaker.data.api.NetworkClient
import com.my.playlistmaker.data.api.TracksRepository
import com.my.playlistmaker.data.api.TracksSearchRequest
import com.my.playlistmaker.domain.api.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                emit(Resource.Success((response as TracksSearchResponse).results))
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }

    }
}
