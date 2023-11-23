package com.my.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

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
    }
}