package com.my.playlistmaker.di

import com.my.playlistmaker.data.api.NetworkClient
import com.my.playlistmaker.data.api.TracksRepository
import com.my.playlistmaker.data.api.impl.RetrofitNetworkClient
import com.my.playlistmaker.data.api.impl.TracksRepositoryImpl
import com.my.playlistmaker.data.search.SearchHistoryRepository
import com.my.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.my.playlistmaker.data.settings.SettingsRepository
import com.my.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.my.playlistmaker.data.sharing.SharingRepository
import com.my.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import org.koin.dsl.module

val dataModule = module {

    single<SharingRepository> {
        SharingRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }


}