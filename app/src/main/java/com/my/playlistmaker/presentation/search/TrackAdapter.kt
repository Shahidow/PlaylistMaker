package com.my.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.my.playlistmaker.R
import com.my.playlistmaker.Track
import com.my.playlistmaker.presentation.player.PlayerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.getKoin
import java.text.SimpleDateFormat
import java.util.*

class TrackAdapter(
    private val trackList: List<Track>,
    private val clickListener: RecyclerViewClickListener,
    private val context: Context
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private var isClickAllowed = true

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView
        private val artistName: TextView
        private val trackTime: TextView
        private val artworkUrl100: ImageView

        init {
            trackName = itemView.findViewById(R.id.trackName)
            artistName = itemView.findViewById(R.id.playerArtistName)
            trackTime = itemView.findViewById(R.id.trackTime)
            artworkUrl100 = itemView.findViewById(R.id.imageTrack)
        }

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

    private fun clickDebounce(coroutineScope:CoroutineScope): Boolean {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_search, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        val gson: Gson = getKoin().get()
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce(coroutineScope)) {
                val libraryIntent = Intent(context, PlayerActivity::class.java)
                libraryIntent.putExtra("trackForPlayer", gson.toJson(trackList[position]))
                clickListener.onItemClicked(trackList[position])
                context.startActivity(libraryIntent)
            }
        }
    }
}