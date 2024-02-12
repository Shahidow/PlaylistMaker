package com.my.playlistmaker.presentation.player.mapper

import com.my.playlistmaker.Track
import com.my.playlistmaker.presentation.player.models.TrackInfo
import java.text.SimpleDateFormat
import java.util.*

object trackMapper {
    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
            artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseYear = track.releaseDate?.takeIf { it.length >= 4 }?.substring(0, 4) ?: "-",
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }
}