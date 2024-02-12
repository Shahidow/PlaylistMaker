package com.my.playlistmaker.data

import android.media.MediaPlayer


class PlayerImpl(): Player {

    companion object {
        private const val STATE_DEFAULT = "STATE_DEFAULT"
        private const val STATE_PREPARED = "STATE_PREPARED"
        private const val STATE_PLAYING = "STATE_PLAYING"
        private const val STATE_PAUSED = "STATE_PAUSED"
    }

    var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun onCreate(trackURL: String) {
        mediaPlayer.setDataSource(trackURL)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }

    override fun play() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pause()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                play()
            }
        }
    }

}