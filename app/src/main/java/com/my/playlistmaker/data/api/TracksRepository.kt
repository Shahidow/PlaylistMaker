package com.my.playlistmaker.data.api

import com.my.playlistmaker.Track
import com.my.playlistmaker.domain.api.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}