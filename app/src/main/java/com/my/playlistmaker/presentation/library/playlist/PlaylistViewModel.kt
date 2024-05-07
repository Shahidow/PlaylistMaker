package com.my.playlistmaker.presentation.library.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.playlistmaker.domain.db.PlaylistInteractor
import com.my.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private var playlists = MutableLiveData<List<Playlist>>()
    val playlistsLiveData: LiveData<List<Playlist>> = playlists

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlistItems -> playlists.postValue(playlistItems) }
        }
    }
}