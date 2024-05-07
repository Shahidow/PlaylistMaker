package com.my.playlistmaker.data.db.dao

import androidx.room.*
import com.my.playlistmaker.data.db.entity.PlaylistTracksEntity

@Dao
interface PlaylistTracksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setTrackToPlaylist(playlistTracksEntity: PlaylistTracksEntity)
    @Query("SELECT * FROM playlist_tracks_table")
    suspend fun getAllTracks(): List<PlaylistTracksEntity>
}