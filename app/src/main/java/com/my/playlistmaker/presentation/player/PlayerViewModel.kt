package com.my.playlistmaker.presentation.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PlayerViewModel : ViewModel() {

    companion object {
        private const val DELAY = 300L
    }

    private var mediaPlayer = MediaPlayer()
    private var playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val playerStateLiveData: LiveData<PlayerState> = playerState
    private var timerJob: Job? = null
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

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

}