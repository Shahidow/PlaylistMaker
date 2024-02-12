package com.my.playlistmaker.presentation.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.playlistmaker.*
import com.my.playlistmaker.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var vm: SearchViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var noResults: LinearLayout
    private lateinit var noInternet: LinearLayout
    private lateinit var historyLayout: LinearLayout
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: TrackAdapter
    private val trackList = ArrayList<Track>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]

        progressBar = binding.progressBar
        recyclerTrack = binding.trackRecycler
        noResults = binding.noresults
        noInternet = binding.nointernet
        historyLayout = binding.searchHistory
        val inputEditText = binding.inputEditText
        val historyRecycler = binding.historyRecycler
        val historyList: ArrayList<Track> = arrayListOf()

        val itemClickListener: RecyclerViewClickListener = object : RecyclerViewClickListener {
            override fun onItemClicked(track: Track) {
                vm.addTrackToHistory(track)
            }
        }

        vm.trackListLiveData.observe(this, Observer {
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
        })

        vm.historyListLiveData.observe(this, Observer {
            historyList.clear()
            historyList.addAll(it)
        })

        adapter = TrackAdapter(trackList, itemClickListener, this)
        recyclerTrack.layoutManager = LinearLayoutManager(this)
        recyclerTrack.adapter = adapter

        historyList.addAll(vm.getHistoryList())
        val historyAdapter = TrackAdapter(historyList, itemClickListener, this)
        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
        historyLayoutVisibility()

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

        binding.backFromSearch.setOnClickListener {
            finish()
        }

        binding.clearIcon.setOnClickListener {
            trackList.clear()
            noInternet.visibility = View.GONE
            noResults.visibility = View.GONE
            adapter.notifyDataSetChanged()
            inputEditText.setText("")
            historyLayoutVisibility()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
        if (vm.getHistoryList().isNotEmpty()) {
            historyLayout.visibility = View.VISIBLE
        }
    }
}