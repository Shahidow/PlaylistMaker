package com.my.playlistmaker.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.my.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.my.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.my.playlistmaker.domain.sharing.impl.SharingInteractorImpl

class SettingsViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val sharingRepository by lazy { SharingRepositoryImpl(context) }
    private val sharingInteractor by lazy { SharingInteractorImpl(sharingRepository) }
    private val settingsRepository by lazy { SettingsRepositoryImpl(context) }
    private val settingsInteractor by lazy { SettingsInteractorImpl(settingsRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(sharingInteractor, settingsInteractor) as T
    }
}