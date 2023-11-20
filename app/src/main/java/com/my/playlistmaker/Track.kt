package com.my.playlistmaker

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String,
    val trackId: Long,
) // Ссылка на изображение обложки

data class TrackResponse(
    val results: List<Track>,
    val resultCount: Int
)
