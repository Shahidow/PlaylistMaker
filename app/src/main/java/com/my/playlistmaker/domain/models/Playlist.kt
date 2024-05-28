package com.my.playlistmaker.domain.models

import java.io.Serializable

data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverUri: String?,
    val trackList: String,
    val amountOfTracks: Int
) : Serializable