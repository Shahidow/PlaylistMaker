package com.my.playlistmaker.presentation.library.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.playlistmaker.Track
import com.my.playlistmaker.domain.db.FavoritesInteractor
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private var tracksList = MutableLiveData<List<Track>>()
    val trackListLiveData: LiveData<List<Track>> = tracksList

    init {
        getTrackList()
    }

    fun onResume() {
        getTrackList()
    }

    fun getTrackList() {
        viewModelScope.launch {
            favoritesInteractor
                .getTracks()
                .collect { data -> tracksList.postValue(data)}
        }
    }
}