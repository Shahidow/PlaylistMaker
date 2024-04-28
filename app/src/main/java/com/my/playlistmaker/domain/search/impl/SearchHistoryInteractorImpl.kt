package com.my.playlistmaker.domain.search.impl

import com.my.playlistmaker.Track
import com.my.playlistmaker.data.search.SearchHistoryRepository
import com.my.playlistmaker.domain.search.SearchHistoryInteractor
import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository):
    SearchHistoryInteractor {
    override fun getList(): Flow<List<Track>> {
        return searchHistoryRepository.getList()
    }

    override fun addTrack(track: Track) {
        searchHistoryRepository.addTrack(track)
    }

    override fun clearList() {
        searchHistoryRepository.clearList()
    }

}