package com.my.playlistmaker.presentation.library.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.playlistmaker.R
import com.my.playlistmaker.databinding.FragmentPlaylistBinding
import com.my.playlistmaker.domain.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private lateinit var recyclerPlaylist: RecyclerView
    private lateinit var adapter: PlaylistAdapter
    private val playlists = ArrayList<Playlist>()
    private val vm by viewModel<PlaylistViewModel>()
    private var binding: FragmentPlaylistBinding? = null

    companion object {
        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.playlistsLiveData.observe(this.viewLifecycleOwner){
            playlists.clear()
            playlists.addAll(it)
            adapter.notifyDataSetChanged()
            checkPlaylists()
        }

        recyclerPlaylist = binding!!.playlistRecycler
        adapter = PlaylistAdapter(playlists)
        recyclerPlaylist.layoutManager = GridLayoutManager(requireActivity().applicationContext, 2)
        recyclerPlaylist.adapter = adapter
        vm.getPlaylists()

        binding?.libraryNewPlaylistButton?.setOnClickListener {
            requireParentFragment().findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
            }
    }

    private fun checkPlaylists(){
        if(playlists.isNullOrEmpty()){
            binding?.playlistsEmpty?.visibility = View.VISIBLE
            recyclerPlaylist.visibility = View.GONE
        } else {
            binding?.playlistsEmpty?.visibility = View.GONE
            recyclerPlaylist.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        vm.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}