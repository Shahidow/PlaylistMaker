package com.my.playlistmaker.data.db

import com.my.playlistmaker.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun setTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
}