package com.my.playlistmaker.presentation.search

import com.my.playlistmaker.Track

interface RecyclerViewClickListener {
    fun onItemClicked(track: Track)
}
