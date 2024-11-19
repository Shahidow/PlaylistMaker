package com.my.playlistmaker.presentation.library.playlist.playlistitem

import com.my.playlistmaker.domain.models.Track

interface PlaylistItemTracksClickListener {
    fun onItemClicked(track: Track)
    fun onItemLongClicked(track: Track)
}