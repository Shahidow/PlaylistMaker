package com.my.playlistmaker.presentation.library.playlist.newplaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.playlistmaker.domain.db.PlaylistInteractor
import com.my.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private var playlistItem = MutableLiveData<Playlist>()
    val playlistLiveData: LiveData<Playlist> = playlistItem

    fun createPlaylist(cover: Uri?, name: String, description: String){
        viewModelScope.launch {
            playlistInteractor
                .createPlaylist(cover, name, description)
        }
    }

    fun checkPlaylist(playlist: Playlist?){
        if (playlist!=null) playlistItem.postValue(playlist!!)
    }

    fun updatePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistInteractor
                .updatePlaylist(playlist)
        }
    }
}