package com.my.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackApiService::class.java)
    private val trackList = ArrayList<Track>()
    private val adapter = TrackAdapter(trackList)
    private var editText: String = ""

    fun readFromSharedPrefs(sharedPreferences: SharedPreferences): Array<Track> {
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

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val back = findViewById<Button>(R.id.backFromSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val recyclerTrack = findViewById<RecyclerView>(R.id.trackRecycler)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val noResults = findViewById<LinearLayout>(R.id.noresults)
        val noInternet = findViewById<LinearLayout>(R.id.nointernet)
        val refresh = findViewById<Button>(R.id.refresh)
        val historyLayout = findViewById<LinearLayout>(R.id.searchHistory)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistory)
        val historyRecycler = findViewById<RecyclerView>(R.id.historyRecycler)
        val historySharedPrefs =
            getSharedPreferences(HISTORY_EXAMPLE_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        searchHistoryList.addList(ArrayList(readFromSharedPrefs(historySharedPrefs).toList()))
        if (searchHistoryList.get().isNotEmpty()){
            historyLayout.visibility = View.VISIBLE
        }
        val historyAdapter = TrackAdapter(searchHistoryList.get())

        recyclerTrack.layoutManager = LinearLayoutManager(this)
        recyclerTrack.adapter = adapter

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter

        var searchText = ""

        searchHistoryList.listen().subscribe { list ->
            val json = Gson().toJson(list)
            historySharedPrefs.edit()
                .putString(HISTORY_KEY, json)
                .apply()
            historyAdapter.notifyDataSetChanged()
        }

        fun getTracks(text: String) {
            trackService.search(text)
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            recyclerTrack.visibility = View.VISIBLE
                            noInternet.visibility = View.GONE
                            noResults.visibility = View.GONE
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        } else {
                            recyclerTrack.visibility = View.GONE
                            noInternet.visibility = View.GONE
                            noResults.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        trackList.clear()
                        adapter.notifyDataSetChanged()
                        recyclerTrack.visibility = View.GONE
                        noResults.visibility = View.GONE
                        noInternet.visibility = View.VISIBLE
                    }
                })
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackList.clear()
                searchText = inputEditText.text.toString()
                if (searchText.isNotEmpty()) {
                    getTracks(searchText)
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
            getTracks(searchText)
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

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                historyLayout.visibility = View.GONE
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                editText = inputEditText.text.toString()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
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
    }
}