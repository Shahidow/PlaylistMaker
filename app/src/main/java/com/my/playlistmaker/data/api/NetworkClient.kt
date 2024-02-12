package com.my.playlistmaker.data.api

interface NetworkClient {
    fun doRequest(dto: Any): Response
}