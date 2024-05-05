package com.my.playlistmaker.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.my.playlistmaker.app.SETTING_EXAMPLE_PREFERENCES
import com.my.playlistmaker.data.api.NetworkClient
import com.my.playlistmaker.data.api.TracksRepository
import com.my.playlistmaker.data.api.impl.RetrofitNetworkClient
import com.my.playlistmaker.data.api.impl.TracksRepositoryImpl
import com.my.playlistmaker.data.db.AppDatabase
import com.my.playlistmaker.data.db.FavoritesRepository
import com.my.playlistmaker.data.db.PlaylistRepository
import com.my.playlistmaker.data.db.impl.FavoritesRepositoryImpl
import com.my.playlistmaker.data.db.impl.PlaylistRepositoryImpl
import com.my.playlistmaker.data.search.SearchHistoryRepository
import com.my.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.my.playlistmaker.data.settings.SettingsRepository
import com.my.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.my.playlistmaker.data.sharing.SharingRepository
import com.my.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<SharedPreferences>{
        androidContext().getSharedPreferences(SETTING_EXAMPLE_PREFERENCES, Application.MODE_PRIVATE)
    }

    single<Gson>{
        Gson()
    }

}
