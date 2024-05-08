package com.my.playlistmaker.presentation.library.playlist.playlistitem

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.my.playlistmaker.R
import com.my.playlistmaker.databinding.FragmentPlaylistItemBinding
import com.my.playlistmaker.domain.models.Playlist
import com.my.playlistmaker.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlaylistItemFragment : Fragment() {

    private lateinit var recyclerTrack: RecyclerView
    private lateinit var adapter: PlaylistItemAdapter
    private var _binding: FragmentPlaylistItemBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModel<PlaylistItemViewModel>()
    private val trackList = ArrayList<Track>()
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private lateinit var confirmDialogPlaylist: MaterialAlertDialogBuilder
    private lateinit var deletedTrack: Track
    private lateinit var playlist: Playlist

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetContainer = binding.MenuBottomSheet
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

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })
        val playlistId = (arguments?.getSerializable("playlist") as Int?)!!
        vm.getPlaylist(playlistId)

        val itemClickListener: PlaylistItemTracksClickListener =
            object : PlaylistItemTracksClickListener {
                override fun onItemClicked(track: Track) {
                    val args = Bundle()
                    args.putSerializable("track", track)
                    findNavController().navigate(
                        R.id.action_playlistItemFragment_to_playerFragment,
                        args
                    )
                }
                override fun onItemLongClicked(track: Track) {
                    deletedTrack = track
                    confirmDialog.show()
                }
            }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNeutralButton("Отмена") { _, _ -> }
            .setNegativeButton("Удалить") { _, _ -> vm.deleteTrack(deletedTrack, playlist) }

        confirmDialogPlaylist = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setNeutralButton("Нет") { _, _ -> }
            .setNegativeButton("Да") { _, _ ->
                run {
                    vm.deletePlaylist(playlist)
                    findNavController().navigateUp()
                }
            }

        recyclerTrack = binding.playlistItemTracksRecycler
        adapter = PlaylistItemAdapter(trackList, itemClickListener)
        recyclerTrack.layoutManager = LinearLayoutManager(requireContext())
        recyclerTrack.adapter = adapter

        vm.tracksLiveData.observe(this.viewLifecycleOwner) {
            trackList.clear()
            trackList.addAll(it)
            binding.totalMinutesInPlaylist.text = tracksTime()
            binding.trackListEmpty.isVisible = trackList.isEmpty()
            binding.playlistItemTracksRecycler.isVisible = trackList.isNotEmpty()
            adapter.notifyDataSetChanged()
        }

        vm.playlistLiveData.observe(this.viewLifecycleOwner) {
            playlist = it
            vm.getTracks(it.trackList)
            binding.numberOfTracks.text =
                resources.getQuantityString(R.plurals.tracks, it.amountOfTracks, it.amountOfTracks)
            binding.PlaylistItemName.text = it.playlistName
            binding.playlistDescriptionGroup.isVisible = it.playlistDescription.isNotEmpty()
            binding.PlaylistItemDescription.text = it.playlistDescription
            Glide.with(requireContext())
                .load(it.playlistCoverUri)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .into(binding.PlaylistItemImage)
            binding.includePlaylistPlayer.playlistNameInPlayer.text = it.playlistName
            binding.includePlaylistPlayer.amountOfTracksInPlayer.text =
                resources.getQuantityString(R.plurals.tracks, it.amountOfTracks, it.amountOfTracks)
            Glide.with(requireContext())
                .load(it.playlistCoverUri)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .into(binding.includePlaylistPlayer.playlistImageInPlayer)
        }

        binding.playlistMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.playlistShare.setOnClickListener {
            if(trackList.isNotEmpty()){
                vm.sharePlaylist(playlistId)
            } else {
                Toast.makeText(requireContext(), "В этом плейлисте нет списка треков, которым можно поделиться", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backFromPlaylistItem.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.menuShareButton.setOnClickListener {
            if(trackList.isNotEmpty()){
                vm.sharePlaylist(playlistId)
            } else {
                Toast.makeText(requireContext(), "В этом плейлисте нет списка треков, которым можно поделиться", Toast.LENGTH_SHORT).show()
            }
        }

        binding.menuDeleteButton.setOnClickListener {
            confirmDialogPlaylist.show()
        }

        binding.menuEditButton.setOnClickListener {
            val args = Bundle()
            args.putSerializable("editPlaylist", playlist)
            findNavController().navigate(
                R.id.action_playlistItemFragment_to_newPlaylistFragment,
                args
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun tracksTime(): String {
        var time: Long = 0
        for (track in trackList) {
            time += track.trackTimeMillis
        }
        val tracksTime = SimpleDateFormat("mm", Locale.getDefault()).format(time)
        return "$tracksTime минут"
    }
}