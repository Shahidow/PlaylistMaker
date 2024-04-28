package com.my.playlistmaker.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.playlistmaker.Track
import com.my.playlistmaker.domain.api.TracksInteractor
import com.my.playlistmaker.domain.search.SearchHistoryInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchText: String? = null
    private var searchJob: Job? = null
    private var trackList = MutableLiveData<SearchState>()
    val trackListLiveData: LiveData<SearchState> = trackList
    private var historyList = MutableLiveData<List<Track>>()
    val historyListLiveData: LiveData<List<Track>> = historyList

    fun searchDebounce(text: String) {
        if (text == searchText || text.isEmpty()) {
            return
        }
        searchText = text
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTracks(searchText!!)
        }
    }

    fun onResume(){
        if(!searchText.isNullOrEmpty()) {searchTracks(searchText!!)}
        getHistoryList()
    }

    fun searchTracks(expression: String) {
        trackList.postValue(SearchState.Loading)
        viewModelScope.launch {
            tracksInteractor
                .searchTracks(expression)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(tracks: List<Track>?, errorMessage: String?) {

        if (tracks != null) {
            trackList.postValue(SearchState.Content(tracks))
        } else if (errorMessage != null) {
            trackList.postValue(SearchState.Error(errorMessage))
        }
    }

    fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addTrack(track)
        getHistoryList()
    }

    fun getHistoryList() {
        viewModelScope.launch {
            searchHistoryInteractor
                .getList()
                .collect { trackList ->
                    historyList.postValue(trackList)
                }
        }
    }

    fun clearHistoryList() {
        searchHistoryInteractor.clearList()
    }

    fun clearSearchText(){
        searchText = null
    }
}