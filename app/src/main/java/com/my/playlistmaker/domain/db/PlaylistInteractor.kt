package com.my.playlistmaker.domain.db

import android.net.Uri
import com.my.playlistmaker.domain.models.Playlist
import com.my.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(cover: Uri?, name: String, description: String)
    suspend fun setPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistItem(id: Int): Flow<Playlist>
    suspend fun setTrack(track: Track)
    fun getAllTracks(): Flow<List<Track>>
    fun getPlaylistTracks(tracksId: String): Flow<List<Track>>
    suspend fun deleteTrack(track: Track, playlist: Playlist)
    suspend fun sharePlaylist(playlistId: Int)
    suspend fun deletePlaylist(playlist: Playlist)
}