package com.my.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackApiService::class.java)
    private val trackList = ArrayList<Track>()
    private val adapter = TrackAdapter(trackList)

    var editText: String = ""

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val back = findViewById<Button>(R.id.backFromSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val recyclerTrack = findViewById<RecyclerView>(R.id.trackRecycler)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val noresults = findViewById<LinearLayout>(R.id.noresults)
        val nointernet = findViewById<LinearLayout>(R.id.nointernet)
        val refresh = findViewById<Button>(R.id.refresh)

        recyclerTrack.layoutManager = LinearLayoutManager(this)
        recyclerTrack.adapter = adapter
        var searchText = ""

        fun getTracks(text: String) {
            trackService.search(text)
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            nointernet.visibility = View.GONE
                            noresults.visibility = View.GONE
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        } else {
                            noresults.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        trackList.clear()
                        adapter.notifyDataSetChanged()
                        nointernet.visibility = View.VISIBLE
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


        refresh.setOnClickListener {
            getTracks(searchText)
        }

        back.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            trackList.clear()
            adapter.notifyDataSetChanged()
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
}