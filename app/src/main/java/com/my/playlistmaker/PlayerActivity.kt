package com.my.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity() : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

    private var isPlay = true
    private val runnable = Runnable { playTimer() }
    private var playTime = 1
    private val handler = Handler(Looper.getMainLooper())
    private var trackURL = ""
    private var playerState = STATE_DEFAULT
    private lateinit var playerTime: TextView
    private lateinit var buttonPlay: ImageView
    private var mediaPlayer = MediaPlayer()

    private fun playTimer() {
        playerTime.text = String.format("%02d:%02d", playTime / 60, playTime % 60) //"00:$playTime"
        playTime++
        timerDebounce()
    }

    private fun clearTimer(){
        playerTime.text = "00:00"
        playTime = 1
        handler.removeCallbacks(runnable)
    }

    private fun timerDebounce() {
        handler.postDelayed(runnable, DELAY)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(trackURL)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            buttonPlay.setImageResource(R.drawable.button_play)
            clearTimer()
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        buttonPlay.setImageResource(R.drawable.button_pause)
        timerDebounce()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        buttonPlay.setImageResource(R.drawable.button_play)
        handler.removeCallbacks(runnable)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerTime = findViewById(R.id.playerTime)
        buttonPlay = findViewById(R.id.playButton)
        val backButton = findViewById<Button>(R.id.backFromPlayer)
        val trackImage = findViewById<ImageView>(R.id.playerTrackImage)
        val trackName = findViewById<TextView>(R.id.playerTrackName)
        val artistName = findViewById<TextView>(R.id.playerArtistName)
        val trackTime = findViewById<TextView>(R.id.playerTrackTime)
        val collectionName = findViewById<TextView>(R.id.playerCollectionName)
        val year = findViewById<TextView>(R.id.playerYear)
        val genre = findViewById<TextView>(R.id.playerGenre)
        val country = findViewById<TextView>(R.id.playerCountry)
        val collectionVisibility = findViewById<Group>(R.id.playerCollectionGroup)
        val trackForPlayer =
            Gson().fromJson(intent.getStringExtra("trackForPlayer"), Track::class.java)
        val artworkUrl512 = trackForPlayer.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        trackURL = trackForPlayer.previewUrl
        preparePlayer()
        trackName.text = trackForPlayer.trackName
        artistName.text = trackForPlayer.artistName
        genre.text = trackForPlayer.primaryGenreName
        country.text = trackForPlayer.country
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackForPlayer.trackTimeMillis)
        Glide.with(applicationContext)
            .load(artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.playerRadius)))
            .into(trackImage)
        year.text = trackForPlayer.releaseDate?.takeIf { it.length >= 4 }?.substring(0, 4) ?: "-"
        if (trackForPlayer.collectionName == "" || trackForPlayer.collectionName == null) {
            collectionVisibility.visibility = View.GONE
        } else {
            collectionName.text = trackForPlayer.collectionName
        }

        backButton.setOnClickListener {
            finish()
        }

        buttonPlay.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}