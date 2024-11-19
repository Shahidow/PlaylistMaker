package com.my.playlistmaker.presentation.library.playlist.playlistitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.my.playlistmaker.R
import com.my.playlistmaker.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PlaylistItemAdapter(private val trackList: List<Track>,
private val clickListener: PlaylistItemTracksClickListener,
) : RecyclerView.Adapter<PlaylistItemAdapter.PlaylistItemViewHolder>() {

    private var isClickAllowed = true

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    class PlaylistItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val artistName: TextView = itemView.findViewById(R.id.playerArtistName)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
        private val artworkUrl100: ImageView = itemView.findViewById(R.id.imageTrack)

        fun bind(model: Track) {
            trackName.text = model.trackName
            artistName.text = model.artistName
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.searchRadius)))
                .into(artworkUrl100)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_search, parent, false)
        return PlaylistItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce(coroutineScope)) {
                clickListener.onItemClicked(trackList[position])
            }
        }
        holder.itemView.setOnLongClickListener {
            clickListener.onItemLongClicked(trackList[position])
            true
        }
    }
}