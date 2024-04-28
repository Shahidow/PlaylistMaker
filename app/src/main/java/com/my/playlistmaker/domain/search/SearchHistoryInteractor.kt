package com.my.playlistmaker.domain.search

import com.my.playlistmaker.Track
import kotlinx.coroutines.flow.Flow


interface SearchHistoryInteractor {
    fun getList(): Flow<List<Track>>
    fun addTrack(track: Track)
    fun clearList()
}