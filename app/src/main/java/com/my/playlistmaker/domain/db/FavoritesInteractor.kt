package com.my.playlistmaker.domain.db

import com.my.playlistmaker.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun setTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
}