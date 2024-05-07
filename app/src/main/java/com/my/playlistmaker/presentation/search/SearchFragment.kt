package com.my.playlistmaker.presentation.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.playlistmaker.R
import com.my.playlistmaker.domain.models.Track
import com.my.playlistmaker.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val vm by viewModel<SearchViewModel>()
    private lateinit var progressBar: ProgressBar
    private lateinit var noResults: LinearLayout
    private lateinit var noInternet: LinearLayout
    private lateinit var historyLayout: LinearLayout
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var adapter: TrackAdapter
    private val trackList = ArrayList<Track>()
    private lateinit var binding: FragmentSearchBinding
    private val historyList: ArrayList<Track> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        recyclerTrack = binding.trackRecycler
        noResults = binding.noresults
        noInternet = binding.nointernet
        historyLayout = binding.searchHistory
        val inputEditText = binding.inputEditText
        val historyRecycler = binding.historyRecycler

        val itemClickListener: RecyclerViewClickListener = object : RecyclerViewClickListener {
            override fun onItemClicked(track: Track) {
                vm.addTrackToHistory(track)
                val args = Bundle()
                args.putSerializable("track", track)
                findNavController().navigate(R.id.action_searchFragment_to_playerFragment, args)
            }
        }

        adapter = TrackAdapter(trackList, itemClickListener, requireContext())
        recyclerTrack.layoutManager = LinearLayoutManager(requireContext())
        recyclerTrack.adapter = adapter

        val historyAdapter = TrackAdapter(historyList, itemClickListener, requireContext())
        historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        historyRecycler.adapter = historyAdapter

        vm.trackListLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is SearchState.Loading -> {
                    showLoading()
                }
                is SearchState.Content -> {
                    showContent(it.trackList)
                }
                is SearchState.Error -> {
                    showError(it.errorMessage)
                }
            }
        }

        vm.historyListLiveData.observe(this.viewLifecycleOwner) {
            historyList.clear()
            historyList.addAll(it)
            historyAdapter.notifyDataSetChanged()
            historyLayoutVisibility()
        }

        vm.getHistoryList()

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val editText = inputEditText.text.toString()
                if (editText.isNotEmpty()) {
                    vm.searchTracks(editText)
                    adapter.notifyDataSetChanged()
                }
                true
            }
            false
        }

        binding.clearHistory.setOnClickListener {
            vm.clearHistoryList()
            historyAdapter.notifyDataSetChanged()
            historyLayout.visibility = View.GONE
        }

        binding.refresh.setOnClickListener {
            vm.searchTracks(inputEditText.text.toString())
        }

        binding.clearIcon.setOnClickListener {
            vm.clearSearchText()
            trackList.clear()
            noInternet.visibility = View.GONE
            noResults.visibility = View.GONE
            recyclerTrack.visibility = View.GONE
            adapter.notifyDataSetChanged()
            inputEditText.setText("")
            historyLayoutVisibility()
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                historyLayout.visibility = View.GONE
                binding.clearIcon.visibility = clearButtonVisibility(s)
                vm.searchDebounce(inputEditText.text.toString())
            }
        })
    }

    private fun showError(errorMessage: String) {
        progressBar.visibility = View.GONE
        recyclerTrack.visibility = View.GONE
        noResults.visibility = View.GONE
        noInternet.visibility = View.VISIBLE
    }

    private fun showContent(trackListData: List<Track>) {
        if (trackListData.isNotEmpty()) {
            trackList.clear()
            trackList.addAll(trackListData)
            progressBar.visibility = View.GONE
            noInternet.visibility = View.GONE
            noResults.visibility = View.GONE
            recyclerTrack.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        } else {
            noInternet.visibility = View.GONE
            recyclerTrack.visibility = View.GONE
            progressBar.visibility = View.GONE
            noResults.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE
        else View.VISIBLE
    }

    private fun historyLayoutVisibility() {
        if (historyList.isNotEmpty() && noInternet.visibility == View.GONE && noResults.visibility == View.GONE && recyclerTrack.visibility == View.GONE) {
            historyLayout.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        vm.onResume()
    }
}