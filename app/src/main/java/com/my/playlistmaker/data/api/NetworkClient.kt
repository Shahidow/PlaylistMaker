package com.my.playlistmaker.data.api

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}