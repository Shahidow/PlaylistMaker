package com.my.playlistmaker.data.db.dao

import androidx.room.*
import com.my.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlaylist(playlistEntity: PlaylistEntity)
    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylist(): List<PlaylistEntity>
}