package com.my.playlistmaker.presentation.search

import com.my.playlistmaker.Track

sealed interface SearchState {
    object Loading: SearchState
    data class Content (val trackList: List<Track>): SearchState
    data class Error (val errorMessage: String): SearchState
}