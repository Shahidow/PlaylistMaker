package com.my.playlistmaker.presentation.player

import com.my.playlistmaker.domain.models.Playlist

interface PlayerRecyclerClickListener {
    fun onItemClicked(playlist: Playlist)
}