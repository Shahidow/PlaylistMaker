package com.my.playlistmaker.data.api

import com.my.playlistmaker.domain.models.Track
import com.my.playlistmaker.domain.api.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}