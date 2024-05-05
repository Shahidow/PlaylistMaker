package com.my.playlistmaker.data.api.impl

import android.content.Context
import com.my.playlistmaker.R
import com.my.playlistmaker.domain.models.Track
import com.my.playlistmaker.data.api.NetworkClient
import com.my.playlistmaker.data.api.TracksRepository
import com.my.playlistmaker.data.api.TracksSearchRequest
import com.my.playlistmaker.data.db.AppDatabase
import com.my.playlistmaker.domain.api.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val context: Context, private val appDatabase: AppDatabase) : TracksRepository {

    private val appContext = context.applicationContext

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(appContext.getString(R.string.internetError)))
            }
            200 -> {
                val trackList = (response as TracksSearchResponse).results
                val idList = appDatabase.trackDao().getItem()
                for(track in trackList) {
                    track.isFavorite = idList.contains(track.trackId)
                }
                emit(Resource.Success(trackList))
            }
            else -> {
                emit(Resource.Error(appContext.getString(R.string.serverError)))
            }
        }

    }
}
