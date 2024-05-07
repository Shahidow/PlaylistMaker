package com.my.playlistmaker.presentation.library.playlist.newplaylist

import com.my.playlistmaker.domain.models.Playlist

interface PlaylistRecyclerClickListener {
    fun onItemClicked(playlist: Playlist)
}