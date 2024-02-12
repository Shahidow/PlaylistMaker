package com.my.playlistmaker.di

import com.my.playlistmaker.presentation.player.PlayerViewModel
import com.my.playlistmaker.presentation.search.SearchViewModel
import com.my.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }

    viewModel<PlayerViewModel> {
        PlayerViewModel()
    }
}