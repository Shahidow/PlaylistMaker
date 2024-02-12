package com.my.playlistmaker.presentation.ui

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
import com.my.playlistmaker.R
import com.my.playlistmaker.Track
import com.my.playlistmaker.data.PlayerImpl
import com.my.playlistmaker.presentation.mapper.trackMapper

class PlayerActivity() : AppCompatActivity() {

    companion object {
        private const val DELAY = 1000L
    }

    private val runnable = Runnable { playTimer() }
    private var playTime = 1
    private val handler = Handler(Looper.getMainLooper())
    private val player = PlayerImpl()
    private lateinit var playerTime: TextView
    private lateinit var buttonPlay: ImageView

    private fun playTimer() {
        playerTime.text = String.format("%02d:%02d", playTime / 60, playTime % 60)
        when(player.playerState){
            "STATE_PLAYING" -> {
                playTime++
                timerDebounce()
            }
            "STATE_PREPARED" -> {
                buttonPlay.setImageResource(R.drawable.button_play)
                clearTimer()
            }
        }
    }

    private fun timerDebounce() {
        handler.postDelayed(runnable, DELAY)
    }

    private fun clearTimer() {
        playerTime.text = "00:00"
        playTime = 1
        handler.removeCallbacks(runnable)
    }

    private fun buttonControl(){
        when(player.playerState){
            "STATE_PLAYING" -> {
                timerDebounce()
                buttonPlay.setImageResource(R.drawable.button_pause)
            }
            "STATE_PAUSED", "STATE_PREPARED", "STATE_DEFAULT" -> {
                handler.removeCallbacks(runnable)
                buttonPlay.setImageResource(R.drawable.button_play)
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
        val trackForPlayer = trackMapper.map(
            Gson().fromJson(
                intent.getStringExtra("trackForPlayer"),
                Track::class.java
            )
        )
        val trackURL = trackForPlayer.previewUrl

        player.onCreate(trackURL)
        trackName.text = trackForPlayer.trackName
        artistName.text = trackForPlayer.artistName
        genre.text = trackForPlayer.primaryGenreName
        country.text = trackForPlayer.country
        trackTime.text = trackForPlayer.trackTime
        Glide.with(applicationContext)
            .load(trackForPlayer.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.playerRadius)))
            .into(trackImage)
        year.text = trackForPlayer.releaseYear
        if (trackForPlayer.collectionName.isNullOrEmpty()) {
            collectionVisibility.visibility = View.GONE
        } else {
            collectionName.text = trackForPlayer.collectionName
        }

        backButton.setOnClickListener {
            finish()
        }

        buttonPlay.setOnClickListener {
            player.playbackControl()
            buttonControl()
        }
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.onDestroy()
    }

}