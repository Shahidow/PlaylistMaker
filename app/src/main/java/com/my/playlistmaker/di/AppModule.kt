package com.my.playlistmaker.di

import com.my.playlistmaker.presentation.library.favorites.FavoritesViewModel
import com.my.playlistmaker.presentation.library.playlist.newplaylist.NewPlaylistViewModel
import com.my.playlistmaker.presentation.library.playlist.PlaylistViewModel
import com.my.playlistmaker.presentation.library.playlist.playlistitem.PlaylistItemViewModel
import com.my.playlistmaker.presentation.player.PlayerViewModel
import com.my.playlistmaker.presentation.search.SearchViewModel
import com.my.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<PlaylistItemViewModel>{
        PlaylistItemViewModel(get(), get())
    }

    viewModel<NewPlaylistViewModel>{
        NewPlaylistViewModel(get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }

    viewModel<PlayerViewModel> {
        PlayerViewModel(get(), get(), get())
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(get())
    }

    viewModel<PlaylistViewModel> {
        PlaylistViewModel(get())
    }
}
