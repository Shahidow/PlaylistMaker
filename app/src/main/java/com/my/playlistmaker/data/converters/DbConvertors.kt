package com.my.playlistmaker.data.converters

import com.my.playlistmaker.domain.models.Track
import com.my.playlistmaker.data.db.entity.PlaylistEntity
import com.my.playlistmaker.data.db.entity.PlaylistTracksEntity
import com.my.playlistmaker.data.db.entity.TrackEntity
import com.my.playlistmaker.domain.models.Playlist

class DbConvertors {
    fun map(track: Track): TrackEntity =
        TrackEntity(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            addingTime = System.currentTimeMillis()
        )

    fun map(trackEntity: TrackEntity): Track =
        Track(
            true,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.trackId,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl
        )

    fun map(playlist: Playlist): PlaylistEntity =
        PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistCoverUri,
            playlist.trackList,
            playlist.amountOfTracks
        )

    fun map(playlistEntity: PlaylistEntity): Playlist =
        Playlist(
            playlistEntity.playlistId,
            playlistEntity.playlistName,
            playlistEntity.playlistDescription,
            playlistEntity.playlistCoverUri,
            playlistEntity.trackList,
            playlistEntity.amountOfTracks
        )

    fun mapToPlaylistTrack(track: Track): PlaylistTracksEntity =
        PlaylistTracksEntity(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            addingTime = System.currentTimeMillis()
        )

    fun mapToTrack(playlistTracksEntity: PlaylistTracksEntity): Track =
        Track(
            true,
            playlistTracksEntity.trackName,
            playlistTracksEntity.artistName,
            playlistTracksEntity.trackTimeMillis,
            playlistTracksEntity.artworkUrl100,
            playlistTracksEntity.trackId,
            playlistTracksEntity.collectionName,
            playlistTracksEntity.releaseDate,
            playlistTracksEntity.primaryGenreName,
            playlistTracksEntity.country,
            playlistTracksEntity.previewUrl
        )
}