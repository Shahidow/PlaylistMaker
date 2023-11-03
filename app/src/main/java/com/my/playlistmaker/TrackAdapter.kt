package com.my.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(
    val trackList: List<Track>
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView
        private val artistName: TextView
        private val trackTime: TextView
        private val artworkUrl100: ImageView

        init {
            trackName = itemView.findViewById(R.id.trackName)
            artistName = itemView.findViewById(R.id.artistName)
            trackTime = itemView.findViewById(R.id.trackTime)
            artworkUrl100 = itemView.findViewById(R.id.imageTrack)
        }

        fun bind(model: Track) {
            trackName.text = model.trackName
            artistName.text = model.artistName
            trackTime.text = model.trackTime
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.searchRadius)))
                .into(artworkUrl100)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_search, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }


}