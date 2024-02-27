package com.my.playlistmaker.presentation.search

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my.playlistmaker.Track
import com.my.playlistmaker.domain.api.TracksInteractor
import com.my.playlistmaker.domain.search.SearchHistoryInteractor

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchText = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks(searchText) }
    private var trackList = MutableLiveData<SearchState>()
    val trackListLiveData: LiveData<SearchState> = trackList
    private var historyList = MutableLiveData<List<Track>>()
    val historyListLiveData: LiveData<List<Track>> = historyList

    fun searchDebounce(text: String) {
        if (text.isNotEmpty()) {
            searchText = text
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun searchTracks(expression: String) {
        trackList.postValue(SearchState.Loading)
        tracksInteractor.searchTracks(expression, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                if (foundTracks != null) {
                    trackList.postValue(SearchState.Content(foundTracks))
                } else if (errorMessage != null) {
                    trackList.postValue(SearchState.Error(errorMessage))
                }
            }
        })
    }

    fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addTrack(track)
        historyList.postValue(getHistoryList())
    }

    fun getHistoryList(): List<Track> {
        return searchHistoryInteractor.getList()
    }


    fun clearHistoryList() {
        searchHistoryInteractor.clearList()
    }

}