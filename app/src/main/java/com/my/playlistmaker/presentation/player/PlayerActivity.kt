package com.my.playlistmaker.presentation.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.my.playlistmaker.R
import com.my.playlistmaker.Track
import com.my.playlistmaker.databinding.ActivityPlayerBinding
import com.my.playlistmaker.presentation.player.mapper.trackMapper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject

class PlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val vm by viewModel<PlayerViewModel>()
    private val gson: Gson by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = gson.fromJson(
            intent.getStringExtra("trackForPlayer"),
            Track::class.java
        )
        val trackForPlayer = trackMapper.map(track)
        val trackURL = trackForPlayer.previewUrl

        vm.playerStateLiveData.observe(this, Observer {
            binding.playButton.isEnabled = it.isPlayButtonEnabled
            when (it.buttonImage) {
                "PAUSE" -> binding.playButton.setImageResource(R.drawable.button_pause)
                else -> binding.playButton.setImageResource(R.drawable.button_play)
            }
            binding.playerTime.text = it.progress
        })

        vm.favoriteLiveData.observe( this, Observer {
            track.isFavorite = it
            favoritesButtonImg(it)
        })

        favoritesButtonImg(track.isFavorite)
        vm.onCreate(trackURL)
        binding.playerTrackName.text = trackForPlayer.trackName
        binding.playerArtistName.text = trackForPlayer.artistName
        binding.playerGenre.text = trackForPlayer.primaryGenreName
        binding.playerCountry.text = trackForPlayer.country
        binding.playerTrackTime.text = trackForPlayer.trackTime
        Glide.with(applicationContext)
            .load(trackForPlayer.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.playerRadius)))
            .into(binding.playerTrackImage)
        binding.playerYear.text = trackForPlayer.releaseYear
        if (trackForPlayer.collectionName.isNullOrEmpty()) binding.playerCollectionGroup.visibility =
            View.GONE
        else binding.playerCollectionName.text = trackForPlayer.collectionName

        binding.backFromPlayer.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            vm.playbackControl()
        }

        binding.favoriteButton.setOnClickListener {
            vm.onFavoriteClicked(track)
        }
    }

    private fun favoritesButtonImg(isFavorite: Boolean) {
        if(isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.favorites)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.favorite)
        }

    }

    override fun onPause() {
        super.onPause()
        vm.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }

}