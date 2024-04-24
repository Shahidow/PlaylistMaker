package com.my.playlistmaker.domain.db.impl

import com.my.playlistmaker.Track
import com.my.playlistmaker.data.db.FavoritesRepository
import com.my.playlistmaker.domain.db.FavoritesInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository): FavoritesInteractor {

    override suspend fun setTrack(track: Track) {
        favoritesRepository.setTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoritesRepository.deleteTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
         return favoritesRepository.getTracks()
    }

}