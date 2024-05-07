package com.my.playlistmaker.presentation.player

import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.my.playlistmaker.domain.models.Track
import com.my.playlistmaker.domain.db.FavoritesInteractor
import com.my.playlistmaker.domain.db.PlaylistInteractor
import com.my.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PlayerViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val gson: Gson
) : ViewModel() {

    companion object {
        private const val DELAY = 300L
    }

    private var mediaPlayer = MediaPlayer()
    private var trackSetState = MutableLiveData<TrackSetState>()
    val trackSetStateLiveData: LiveData<TrackSetState> = trackSetState
    private var playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val playerStateLiveData: LiveData<PlayerState> = playerState
    private var timerJob: Job? = null
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val isFavorite = MutableLiveData<Boolean>()
    val favoriteLiveData: LiveData<Boolean> = isFavorite
    private var playlists = MutableLiveData<List<Playlist>>()
    val playlistsLiveData: LiveData<List<Playlist>> = playlists

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoritesInteractor.deleteTrack(track)
                isFavorite.postValue(false)
            } else {
                favoritesInteractor.setTrack(track)
                isFavorite.postValue(true)
            }
        }
    }

    fun onCreate(trackURL: String) {
        mediaPlayer.setDataSource(trackURL)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { playerState.postValue(PlayerState.Prepared()) }
        mediaPlayer.setOnCompletionListener {
            playerState.postValue(PlayerState.Prepared())
            timerJob?.cancel()
        }
    }

    fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState.value = PlayerState.Default()
    }

    fun playbackControl() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pause()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                play()
            }
            else -> {}
        }
    }

    fun play() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        playTimer()
    }

    fun pause() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun playTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(DELAY)
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return dateFormat.format(mediaPlayer.currentPosition) ?: "00:00"
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { playlistItems -> playlists.postValue(playlistItems) }
        }
    }

    fun checkTrack(track: Track, playlist: Playlist) {
        val json = playlist.trackList
        val playlistTracks = gson.fromJson(json, Array<Long>::class.java).toMutableList()
        if(playlistTracks.contains(track.trackId)) {
            trackSetState.postValue(TrackSetState.Negative(playlist.playlistName))
        } else {
            viewModelScope.launch {
                playlistInteractor
                    .setTrack(track)
            }
            playlistTracks.add(track.trackId)
            val newPlaylistTracks = gson.toJson(playlistTracks)
            val amountOfTracks = playlist.amountOfTracks + 1
            viewModelScope.launch {
                playlistInteractor
                    .updatePlaylist(
                        Playlist(
                            playlist.playlistId,
                            playlist.playlistName,
                            playlist.playlistDescription,
                            playlist.playlistCoverUri,
                            newPlaylistTracks,
                            amountOfTracks
                    ))
            }
            trackSetState.postValue(TrackSetState.Success(playlist.playlistName))
        }
    }
}