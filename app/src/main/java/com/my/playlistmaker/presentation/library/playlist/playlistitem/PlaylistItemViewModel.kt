package com.my.playlistmaker.presentation.library.playlist.playlistitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.my.playlistmaker.domain.db.PlaylistInteractor
import com.my.playlistmaker.domain.models.Playlist
import com.my.playlistmaker.domain.models.Track
import kotlinx.coroutines.launch

class PlaylistItemViewModel(
    private val playlistInteractor: PlaylistInteractor, private val gson: Gson
): ViewModel() {

    private var playlist = MutableLiveData<Playlist>()
    val playlistLiveData: LiveData<Playlist> = playlist
    private var tracks = MutableLiveData<List<Track>>()
    val tracksLiveData: LiveData<List<Track>> = tracks

    fun getPlaylist(id: Int){
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistItem(id)
                .collect{playlistItem -> playlist.postValue(playlistItem)}
        }
    }

    fun getTracks(tracksId: String) {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistTracks(tracksId)
                .collect {trackList -> tracks.postValue(trackList)}
        }
    }

    fun deleteTrack(track:Track, playlist:Playlist){
        viewModelScope.launch {
            playlistInteractor
                .deleteTrack(track, playlist)
            getPlaylist(playlist.playlistId)
        }
    }

    fun deletePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistInteractor
                .deletePlaylist(playlist)
        }
    }

    fun sharePlaylist(playlistId: Int){
        viewModelScope.launch {
            playlistInteractor
                .sharePlaylist(playlistId)
        }
    }
}