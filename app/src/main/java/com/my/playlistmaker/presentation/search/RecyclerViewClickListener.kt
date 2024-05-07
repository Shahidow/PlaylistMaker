package com.my.playlistmaker.presentation.search

import com.my.playlistmaker.domain.models.Track

interface RecyclerViewClickListener {
    fun onItemClicked(track: Track)
}
