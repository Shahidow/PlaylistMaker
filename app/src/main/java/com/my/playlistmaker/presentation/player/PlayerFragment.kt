package com.my.playlistmaker.presentation.player

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.my.playlistmaker.R
import com.my.playlistmaker.domain.models.Track
import com.my.playlistmaker.databinding.FragmentPlayerBinding
import com.my.playlistmaker.domain.models.Playlist
import com.my.playlistmaker.presentation.library.playlist.PlaylistAdapter
import com.my.playlistmaker.presentation.player.mapper.trackMapper
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment() : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val vm by viewModel<PlayerViewModel>()
    private val playlists = ArrayList<Playlist>()
    private lateinit var recyclerPlaylist: RecyclerView
    private lateinit var adapter: PlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val track = (arguments?.getSerializable("track") as Track?)!!
        val itemClickListener: PlayerRecyclerClickListener = object : PlayerRecyclerClickListener {
            override fun onItemClicked(playlist: Playlist) {
                vm.checkTrack(track, playlist)
            }
        }
        val trackForPlayer = trackMapper.map(track)
        val trackURL = trackForPlayer.previewUrl
        val bottomSheetContainer = binding.standardBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {binding.overlay.alpha = slideOffset }
        })

        recyclerPlaylist = binding.playlistInPlayerRecycler
        adapter = PlayerAdapter(playlists, itemClickListener)
        recyclerPlaylist.layoutManager = LinearLayoutManager(requireContext())
        recyclerPlaylist.adapter = adapter
        vm.getPlaylists()

        vm.trackSetStateLiveData.observe(this.viewLifecycleOwner){
            val playlistName = it.playlistName
            if (it.result) {
                Toast.makeText(requireContext(), "Добавлено в плейлист [$playlistName]", Toast.LENGTH_SHORT).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                Toast.makeText(requireContext(), "Трек уже добавлен в плейлист [$playlistName]", Toast.LENGTH_SHORT).show()
            }
        }

        vm.playlistsLiveData.observe(this.viewLifecycleOwner) {
            playlists.clear()
            playlists.addAll(it)
            adapter.notifyDataSetChanged()
        }

        vm.playerStateLiveData.observe(this.viewLifecycleOwner) {
            binding.playButton.isEnabled = it.isPlayButtonEnabled
            when (it.buttonImage) {
                "PAUSE" -> binding.playButton.setImageResource(R.drawable.button_pause)
                else -> binding.playButton.setImageResource(R.drawable.button_play)
            }
            binding.playerTime.text = it.progress
        }

        vm.favoriteLiveData.observe(this.viewLifecycleOwner) {
            track.isFavorite = it
            favoritesButtonImg(it)
        }

        favoritesButtonImg(track.isFavorite)
        vm.onCreate(trackURL)
        binding.playerTrackName.text = trackForPlayer.trackName
        binding.playerArtistName.text = trackForPlayer.artistName
        binding.playerGenre.text = trackForPlayer.primaryGenreName
        binding.playerCountry.text = trackForPlayer.country
        binding.playerTrackTime.text = trackForPlayer.trackTime
        Glide.with(requireContext())
            .load(trackForPlayer.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.playerRadius)))
            .into(binding.playerTrackImage)
        binding.playerYear.text = trackForPlayer.releaseYear
        if (trackForPlayer.collectionName.isNullOrEmpty()) binding.playerCollectionGroup.visibility =
            View.GONE
        else binding.playerCollectionName.text = trackForPlayer.collectionName

        binding.backFromPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            vm.playbackControl()
        }

        binding.favoriteButton.setOnClickListener {
            vm.onFavoriteClicked(track)
        }

        binding.playListButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            vm.getPlaylists()
        }

        binding.playerNewPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }

    private fun favoritesButtonImg(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.favorites)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.favorite)
        }

    }

    override fun onPause() {
        super.onPause()
        vm.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }
}