package com.my.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchHistoryList = SearchHistoryList()

class SearchActivity : AppCompatActivity() {
    private lateinit var adapter: TrackAdapter
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var noResults: LinearLayout
    private lateinit var noInternet: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var progressBar: ProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { getTracks() }
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackApiService::class.java)
    private val trackList = ArrayList<Track>()
    private var editText: String = ""

    private fun readFromSharedPrefs(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, editText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        editText = savedInstanceState.getString(SEARCH_TEXT, "")
        inputEditText.setText(editText)
    }

    private fun getTracks() {
        trackList.clear()
        if (inputEditText.text.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            trackService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            progressBar.visibility = View.GONE
                            recyclerTrack.visibility = View.VISIBLE
                            noInternet.visibility = View.GONE
                            noResults.visibility = View.GONE
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        } else {
                            progressBar.visibility = View.GONE
                            recyclerTrack.visibility = View.GONE
                            noInternet.visibility = View.GONE
                            noResults.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        trackList.clear()
                        progressBar.visibility = View.GONE
                        recyclerTrack.visibility = View.GONE
                        noResults.visibility = View.GONE
                        noInternet.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        progressBar = findViewById(R.id.progressBar)
        recyclerTrack = findViewById(R.id.trackRecycler)
        noResults = findViewById(R.id.noresults)
        noInternet = findViewById(R.id.nointernet)
        inputEditText = findViewById(R.id.inputEditText)
        val back = findViewById<Button>(R.id.backFromSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val refresh = findViewById<Button>(R.id.refresh)
        val historyLayout = findViewById<LinearLayout>(R.id.searchHistory)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistory)
        val historyRecycler = findViewById<RecyclerView>(R.id.historyRecycler)
        val historySharedPrefs =
            getSharedPreferences(HISTORY_EXAMPLE_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        searchHistoryList.addList(ArrayList(readFromSharedPrefs(historySharedPrefs).toList()))
        if (searchHistoryList.get().isNotEmpty()) {
            historyLayout.visibility = View.VISIBLE
        }
        adapter = TrackAdapter(trackList, this)
        val historyAdapter = TrackAdapter(searchHistoryList.get(), this)

        recyclerTrack.layoutManager = LinearLayoutManager(this)
        recyclerTrack.adapter = adapter

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter


        searchHistoryList.listen().subscribe { list ->
            val json = Gson().toJson(list)
            historySharedPrefs.edit()
                .putString(HISTORY_KEY, json)
                .apply()
            historyAdapter.notifyDataSetChanged()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editText = inputEditText.text.toString()
                if (editText.isNotEmpty()) {
                    getTracks()
                    adapter.notifyDataSetChanged()
                }
                true
            }
            false
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryList.clear()
            historyAdapter.notifyDataSetChanged()
            historyLayout.visibility = View.GONE
        }

        refresh.setOnClickListener {
            getTracks()
        }

        back.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            trackList.clear()
            noInternet.visibility = View.GONE
            noResults.visibility = View.GONE
            adapter.notifyDataSetChanged()
            inputEditText.setText("")
            historyLayout.visibility = View.VISIBLE
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                historyLayout.visibility = View.GONE
                clearButton.visibility = clearButtonVisibility(s)
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                editText = inputEditText.text.toString()
            }
        }
        )
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}