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
    @Query("SELECT * FROM playlist_table WHERE playlistId = :id")
    suspend fun getPlaylistItem(id: Int): PlaylistEntity
    @Query("DELETE FROM playlist_table WHERE playlistId = :id")
    suspend fun deletePlaylist(id: Int)
}