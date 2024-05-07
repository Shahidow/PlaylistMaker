package com.my.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverUri: String?,
    val trackList: String,
    val amountOfTracks: Int
)
