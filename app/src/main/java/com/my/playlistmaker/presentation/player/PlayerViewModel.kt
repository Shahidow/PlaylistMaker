package com.my.playlistmaker.presentation.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {

    companion object {
        private const val DELAY = 1000L
    }

    private val runnable = Runnable { playTimer() }
    private var playTime = 1
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer = MediaPlayer()
    private var playerState = MutableLiveData<PlayerState>()
    val playerStateLiveData: LiveData<PlayerState> = playerState
    private var playerTime = MutableLiveData<String>()
    val playerTimeLiveData: LiveData<String> = playerTime

    fun onCreate(trackURL: String) {
        mediaPlayer.setDataSource(trackURL)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { playerState.postValue(PlayerState.STATE_PREPARED) }
        mediaPlayer.setOnCompletionListener { playerState.postValue(PlayerState.STATE_PREPARED) }
    }

    fun onDestroy() {
        mediaPlayer.release()
    }

    fun playbackControl() {
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                pause()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                play()
            }
            else -> {}
        }
    }

    fun play() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.STATE_PLAYING)
        timerDebounce()
    }

    fun pause() {
        mediaPlayer.pause()
        playerState.postValue(PlayerState.STATE_PAUSED)
        handler.removeCallbacks(runnable)
    }

    private fun timerDebounce() {
        handler.postDelayed(runnable, DELAY)
    }

    private fun playTimer() {
        playerTime.postValue(String.format("%02d:%02d", playTime / 60, playTime % 60))
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                playTime++
                timerDebounce()
            }
            PlayerState.STATE_PREPARED -> {
                clearTimer()
            }
            else -> {}
        }
    }

    private fun clearTimer() {
        playerTime.postValue("00:00")
        playTime = 1
        handler.removeCallbacks(runnable)
    }
}