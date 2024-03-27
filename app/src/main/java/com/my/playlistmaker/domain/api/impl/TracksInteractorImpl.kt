package com.my.playlistmaker.domain.api.impl

import com.my.playlistmaker.Track
import com.my.playlistmaker.data.api.TracksRepository
import com.my.playlistmaker.domain.api.Resource
import com.my.playlistmaker.domain.api.TracksInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}