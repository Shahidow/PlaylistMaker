package com.my.playlistmaker.presentation.library.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.my.playlistmaker.R
import com.my.playlistmaker.domain.models.Playlist
import com.my.playlistmaker.presentation.library.playlist.newplaylist.PlaylistRecyclerClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistAdapter(private val playlist: List<Playlist>, private val clickListener: PlaylistRecyclerClickListener) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    private var isClickAllowed = true

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playlistName: TextView
        private val playlistCoverImage: ImageView
        private val amountOfTracks: TextView

        init {
            playlistName = itemView.findViewById(R.id.name_of_item_playlist)
            amountOfTracks = itemView.findViewById(R.id.amount_of_tracks)
            playlistCoverImage = itemView.findViewById(R.id.playlist_image_of_item)
        }

        fun bind(model: Playlist) {
            playlistName.text = model.playlistName
            amountOfTracks.text = trackText(model.amountOfTracks)
            Glide.with(itemView)
                .load(model.playlistCoverUri)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.playerRadius)))
                .into(playlistCoverImage)
        }

        private fun trackText(amountOfTracks: Int): String {
            when (amountOfTracks % 100) {
                11,12,13,14 -> return "$amountOfTracks треков"
            }
            return when (amountOfTracks % 10) {
                1 -> "$amountOfTracks трек"
                2, 3, 4 -> "$amountOfTracks трека"
                else -> "$amountOfTracks треков"
            }
        }
    }

    private fun clickDebounce(coroutineScope: CoroutineScope): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            coroutineScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        holder.bind(playlist[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce(coroutineScope)) {
                clickListener.onItemClicked(playlist[position])
            }
        }
    }
}