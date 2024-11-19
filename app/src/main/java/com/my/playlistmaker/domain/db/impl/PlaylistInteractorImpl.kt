package com.my.playlistmaker.domain.db.impl

import android.net.Uri
import com.my.playlistmaker.data.db.PlaylistRepository
import com.my.playlistmaker.domain.db.PlaylistInteractor
import com.my.playlistmaker.domain.models.Playlist
import com.my.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository):PlaylistInteractor {
    override suspend fun createPlaylist(cover: Uri?, name: String, description: String) {
        playlistRepository.createPlaylist(cover, name, description)
    }

    override suspend fun setPlaylist(playlist: Playlist) {
        playlistRepository.setPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun getPlaylistItem(id: Int): Flow<Playlist> {
        return playlistRepository.getPlaylistItem(id)
    }

    override suspend fun setTrack(track: Track) {
        playlistRepository.setTrack(track)
    }

    override fun getAllTracks(): Flow<List<Track>> {
        return playlistRepository.getAllTracks()
    }

    override fun getPlaylistTracks(tracksId: String): Flow<List<Track>> {
        return playlistRepository.getPlaylistTracks(tracksId)
    }

    override suspend fun deleteTrack(track: Track, playlist: Playlist) {
        playlistRepository.deleteTrack(track, playlist)
    }

    override suspend fun sharePlaylist(playlistId: Int) {
        playlistRepository.sharePlaylist(playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }
}