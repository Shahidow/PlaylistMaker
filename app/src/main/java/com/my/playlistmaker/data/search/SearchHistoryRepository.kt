package com.my.playlistmaker.data.search

import com.my.playlistmaker.Track

interface SearchHistoryRepository {
    fun getList(): List<Track>
    fun addTrack(track: Track)
    fun clearList()
}