package com.my.playlistmaker.data.db.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson
import com.my.playlistmaker.R
import com.my.playlistmaker.data.converters.DbConvertors
import com.my.playlistmaker.data.db.AppDatabase
import com.my.playlistmaker.data.db.PlaylistRepository
import com.my.playlistmaker.data.db.entity.PlaylistEntity
import com.my.playlistmaker.data.db.entity.PlaylistTracksEntity
import com.my.playlistmaker.domain.models.Playlist
import com.my.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertors: DbConvertors,
    private val gson: Gson,
    private val context: Context
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

    override fun getPlaylistItem(id: Int): Flow<Playlist> = flow {
        val playlistEntity = appDatabase.playListDao().getPlaylistItem(id)
        val playlist = dbConvertors.map(playlistEntity)
        emit(playlist)
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

    override fun getPlaylistTracks(tracksId: String): Flow<List<Track>> = flow {
        val tracks = appDatabase.playlistTracksDao().getAllTracks()
        val playlistTracks = ArrayList<Track>()
        playlistTracks.addAll(convertFromPlaylistTrack(tracks.sortedByDescending { it.addingTime }))
        val playlistTracksId = gson.fromJson(tracksId, Array<Long>::class.java)
        val filteredTracks =
            playlistTracks.filter { track -> playlistTracksId.contains(track.trackId) }
        val idList = appDatabase.trackDao().getItem()
        for (track in filteredTracks) {
            track.isFavorite = idList.contains(track.trackId)
        }
        emit(filteredTracks)
    }

    override suspend fun deleteTrack(track: Track, playlist: Playlist) {
        val json = playlist.trackList
        val playlistTracks = gson.fromJson(json, Array<Long>::class.java).toMutableList()
        playlistTracks.removeIf { it == track.trackId }
        val newPlaylistTracks = gson.toJson(playlistTracks)
        val amountOfTracks = playlist.amountOfTracks - 1
        appDatabase.playListDao().updatePlaylist(
            PlaylistEntity(
                playlist.playlistId,
                playlist.playlistName,
                playlist.playlistDescription,
                playlist.playlistCoverUri,
                newPlaylistTracks,
                amountOfTracks
            )
        )
        val playlists = appDatabase.playListDao().getPlaylist()
        if (checkTrack(track.trackId, playlists)) {
            appDatabase.playlistTracksDao().deleteTrack(dbConvertors.mapToPlaylistTrack(track))
        }
    }

    override suspend fun sharePlaylist(playlistId: Int) {
        var message = ""
        val playlistEntity = appDatabase.playListDao().getPlaylistItem(playlistId)
        val trackList = getPlaylistTracks(playlistEntity.trackList)
        trackList.collect {message = sharedText(playlistEntity, it)}
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playListDao().deletePlaylist(playlist.playlistId)
        val trackList = gson.fromJson(playlist.trackList, Array<Long>::class.java)
        val playlists = appDatabase.playListDao().getPlaylist()
        trackList.forEach {
            if (checkTrack(it, playlists)) appDatabase.playlistTracksDao().deleteTrackById(it)
        }
    }

    private fun sharedText(playlistEntity: PlaylistEntity, trackList: List<Track>): String {
        var number = 1
        var text = playlistEntity.playlistName + "\n${playlistEntity.playlistDescription}" + "\n${
            context.resources.getQuantityString(
                R.plurals.tracks,
                playlistEntity.amountOfTracks,
                playlistEntity.amountOfTracks
            )
        }"

        trackList.forEach {
            text += "\n${number}.${it.artistName} - ${it.trackName} ${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it.trackTimeMillis)
            }"
            number++
        }
        return text
    }

    private fun checkTrack(trackid: Long, playlists: List<PlaylistEntity>): Boolean {
        for (playlist in playlists) {
            val tracks = gson.fromJson(playlist.trackList, Array<Long>::class.java)
            if (tracks.contains(trackid)) return false
        }
        return true
    }

    private fun convertFromPlaylistTrack(tracks: List<PlaylistTracksEntity>): List<Track> {
        return tracks.map { track -> dbConvertors.mapToTrack(track) }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> dbConvertors.map(playlist) }
    }
}