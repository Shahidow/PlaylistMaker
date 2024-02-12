package com.my.playlistmaker.di

import com.my.playlistmaker.domain.api.TracksInteractor
import com.my.playlistmaker.domain.api.impl.TracksInteractorImpl
import com.my.playlistmaker.domain.search.SearchHistoryInteractor
import com.my.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.my.playlistmaker.domain.settings.SettingsInteractor
import com.my.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.my.playlistmaker.domain.sharing.SharingInteractor
import com.my.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module {

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