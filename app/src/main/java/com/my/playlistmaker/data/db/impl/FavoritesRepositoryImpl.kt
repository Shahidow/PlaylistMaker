package com.my.playlistmaker.data.db.impl

import com.my.playlistmaker.domain.models.Track
import com.my.playlistmaker.data.converters.DbConvertors
import com.my.playlistmaker.data.db.AppDatabase
import com.my.playlistmaker.data.db.FavoritesRepository
import com.my.playlistmaker.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertors: DbConvertors
) : FavoritesRepository {

    override suspend fun setTrack(track: Track) {
        val trackEntity = dbConvertors.map(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrack(track: Track) {
        val trackEntity = dbConvertors.map(track)
        appDatabase.trackDao().deleteTrackEntity(trackEntity)
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        val trackList = convertFromTrackEntity(tracks.sortedByDescending { it.addingTime })
        emit(trackList)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> dbConvertors.map(track) }
    }

}