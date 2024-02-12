package com.my.playlistmaker.domain.search

import com.my.playlistmaker.Track


interface SearchHistoryInteractor {
    fun getList(): List<Track>
    fun addTrack(track: Track)
    fun clearList()
}