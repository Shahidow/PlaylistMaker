package com.my.playlistmaker.data.db.impl

import android.net.Uri
import com.google.gson.Gson
import com.my.playlistmaker.data.converters.DbConvertors
import com.my.playlistmaker.data.db.AppDatabase
import com.my.playlistmaker.data.db.PlaylistRepository
import com.my.playlistmaker.data.db.entity.PlaylistEntity
import com.my.playlistmaker.data.db.entity.PlaylistTracksEntity
import com.my.playlistmaker.domain.models.Playlist
import com.my.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertors: DbConvertors,
    private val gson: Gson
) : PlaylistRepository {

    override suspend fun createPlaylist(cover: Uri?, name: String, description: String) {
        val playlistEntity = PlaylistEntity(
            playlistName = name,
            playlistDescription = description,
            playlistCoverUri = cover?.toString(),
            trackList = gson.toJson(emptyList<Int>()),
            amountOfTracks = 0
        )
        appDatabase.playListDao().setPlaylist(playlistEntity)
    }

    override suspend fun setPlaylist(playlist: Playlist) {
        val playlistEntity = dbConvertors.map(playlist)
        appDatabase.playListDao().setPlaylist(playlistEntity)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = dbConvertors.map(playlist)
        appDatabase.playListDao().updatePlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlistsEntity = appDatabase.playListDao().getPlaylist()
        val playlists = convertFromPlaylistEntity(playlistsEntity)
        emit(playlists)
    }

    override suspend fun setTrack(track: Track) {
        val trackEntity = dbConvertors.mapToPlaylistTrack(track)
        appDatabase.playlistTracksDao().setTrackToPlaylist(trackEntity)
    }

    override fun getAllTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.playlistTracksDao().getAllTracks()
        val trackList = convertFromPlaylistTrack(tracks.sortedByDescending { it.addingTime })
        emit(trackList)
    }

    private fun convertFromPlaylistTrack(tracks: List<PlaylistTracksEntity>): List<Track> {
        return tracks.map { track -> dbConvertors.mapToTrack(track) }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> dbConvertors.map(playlist) }
    }
}