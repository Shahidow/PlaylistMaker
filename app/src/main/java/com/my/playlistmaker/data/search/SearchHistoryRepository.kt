package com.my.playlistmaker.data.search

import com.my.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getList(): Flow<List<Track>>
    fun addTrack(track: Track)
    fun clearList()
}