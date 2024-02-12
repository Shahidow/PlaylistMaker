package com.my.playlistmaker.presentation.player.models

data class TrackInfo(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl512: String, // Ссылка на изображение обложки
    val trackId: Long,
    val collectionName: String?,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String, // Ссылка на отрывок трека
)