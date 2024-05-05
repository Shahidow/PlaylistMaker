package com.my.playlistmaker.presentation.library.playlist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.playlistmaker.domain.db.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    fun createPlaylist(cover: Uri?, name: String, description: String){
        viewModelScope.launch {
            playlistInteractor
                .createPlaylist(cover, name, description)
        }
    }
}