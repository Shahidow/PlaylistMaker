package com.my.playlistmaker.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.playlistmaker.data.api.impl.RetrofitNetworkClient
import com.my.playlistmaker.data.api.impl.TracksRepositoryImpl
import com.my.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.my.playlistmaker.domain.api.impl.TracksInteractorImpl
import com.my.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl

class SearchViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val searchHistoryRepository by lazy { SearchHistoryRepositoryImpl(context) }
    private val searchHistoryInteractor by lazy { SearchHistoryInteractorImpl(searchHistoryRepository) }
    private val tracksRepository by lazy {TracksRepositoryImpl(RetrofitNetworkClient(context))}
    private val tracksInteractor by lazy {TracksInteractorImpl(tracksRepository)}


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchHistoryInteractor, tracksInteractor) as T
    }
}