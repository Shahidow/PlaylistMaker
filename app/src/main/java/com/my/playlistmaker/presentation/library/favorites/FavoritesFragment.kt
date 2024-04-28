package com.my.playlistmaker.presentation.library.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.playlistmaker.Track
import com.my.playlistmaker.databinding.FragmentFavoritesBinding
import com.my.playlistmaker.presentation.search.RecyclerViewClickListener
import com.my.playlistmaker.presentation.search.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    private val vm by viewModel<FavoritesViewModel>()
    private var binding: FragmentFavoritesBinding? = null
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var adapter: TrackAdapter
    private val trackList = ArrayList<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemClickListener: RecyclerViewClickListener = object : RecyclerViewClickListener {
            override fun onItemClicked(track: Track) { }
        }

        vm.getTrackList()
        recyclerTrack = binding!!.favoritesRecycler
        adapter = TrackAdapter(trackList, itemClickListener, requireContext())
        recyclerTrack.layoutManager = LinearLayoutManager(requireContext())
        recyclerTrack.adapter = adapter

        vm.trackListLiveData.observe(this.viewLifecycleOwner) {
            if (it.isEmpty()) {
                trackList.clear()
                adapter.notifyDataSetChanged()
                recyclerTrack.visibility = View.GONE
                binding!!.favoritesEmpty.visibility = View.VISIBLE
            } else {
                trackList.clear()
                trackList.addAll(it)
                adapter.notifyDataSetChanged()
                binding!!.favoritesEmpty.visibility = View.GONE
                recyclerTrack.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}