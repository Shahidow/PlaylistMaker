package com.my.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.playlistmaker.data.db.dao.PlaylistDao
import com.my.playlistmaker.data.db.dao.PlaylistTracksDao
import com.my.playlistmaker.data.db.dao.TrackDao
import com.my.playlistmaker.data.db.entity.PlaylistEntity
import com.my.playlistmaker.data.db.entity.PlaylistTracksEntity
import com.my.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTracksEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlaylistDao
    abstract fun playlistTracksDao(): PlaylistTracksDao
}