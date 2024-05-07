package com.my.playlistmaker.di

import com.my.playlistmaker.data.converters.DbConvertors
import com.my.playlistmaker.domain.api.TracksInteractor
import com.my.playlistmaker.domain.api.impl.TracksInteractorImpl
import com.my.playlistmaker.domain.db.FavoritesInteractor
import com.my.playlistmaker.domain.db.PlaylistInteractor
import com.my.playlistmaker.domain.db.impl.FavoritesInteractorImpl
import com.my.playlistmaker.domain.db.impl.PlaylistInteractorImpl
import com.my.playlistmaker.domain.search.SearchHistoryInteractor
import com.my.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.my.playlistmaker.domain.settings.SettingsInteractor
import com.my.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.my.playlistmaker.domain.sharing.SharingInteractor
import com.my.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module {

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    factory { DbConvertors() }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }
}
