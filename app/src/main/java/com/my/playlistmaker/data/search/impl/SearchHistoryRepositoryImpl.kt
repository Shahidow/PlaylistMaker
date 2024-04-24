package com.my.playlistmaker.data.search.impl

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.my.playlistmaker.Track
import com.my.playlistmaker.data.api.impl.TracksSearchResponse
import com.my.playlistmaker.data.db.AppDatabase
import com.my.playlistmaker.data.search.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.getKoin

const val HISTORY_EXAMPLE_PREFERENCES = "history_example_preferences"
const val HISTORY_KEY = "key_for_history_switch"

class SearchHistoryRepositoryImpl(context: Context, private val appDatabase: AppDatabase) :
    SearchHistoryRepository {

    private val historySharedPrefs =
        context.getSharedPreferences(HISTORY_EXAMPLE_PREFERENCES, Context.MODE_PRIVATE)

    companion object {
        const val SEARCH_HISTORY_LIST_SIZE = 9
    }

    override fun getList(): Flow<List<Track>> = flow {
        val trackList = getSharedPrefs().toList()
        val idList = appDatabase.trackDao().getItem()
        for (track in trackList) {
            track.isFavorite = idList.contains(track.trackId)
        }
        emit(trackList)
    }

    override fun addTrack(track: Track) {
        val historyList = getSharedPrefs().toMutableList()
        if (historyList.isNotEmpty()) {
            if (historyList.contains(track)) {
                historyList.remove(track)
            }
            if (historyList.size > SEARCH_HISTORY_LIST_SIZE) {
                historyList.removeLast()
            }
        }
        historyList.add(0, track)
        setSharedPrefs(historyList)
    }

    override fun clearList() {
        clearSharedPrefs()
    }

    private fun getSharedPrefs(): Array<Track> {
        val json = historySharedPrefs.getString(HISTORY_KEY, null) ?: return emptyArray()
        val gson: Gson = getKoin().get()
        return gson.fromJson(json, Array<Track>::class.java)
    }

    private fun setSharedPrefs(list: List<Track>) {
        historySharedPrefs.edit().putString(HISTORY_KEY, Gson().toJson(list)).apply()
    }

    private fun clearSharedPrefs() {
        historySharedPrefs.edit().clear().apply()
    }
}
