package com.my.playlistmaker.domain.models

import java.io.Serializable

data class Track(
    var isFavorite: Boolean = false,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackId: Long,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String, // Ссылка на отрывок трека
) : Serializable

data class TrackResponse(
    val results: List<Track>,
    val resultCount: Int
)
