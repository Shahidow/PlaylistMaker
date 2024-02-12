package com.my.playlistmaker.presentation.settings

import androidx.lifecycle.ViewModel
import com.my.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.my.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.my.playlistmaker.domain.settings.SettingsInteractor
import com.my.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.my.playlistmaker.domain.sharing.SharingInteractor
import com.my.playlistmaker.domain.sharing.impl.SharingInteractorImpl

class SettingsViewModel(
    private  val sharingInteractor: SharingInteractor,
    private  val settingsInteractor: SettingsInteractor
) : ViewModel() {


    var themeState = settingsInteractor.getThemeState()

    override fun onCleared() {
        super.onCleared()
    }

    fun themeSwitcher(checked: Boolean){
        settingsInteractor.setThemeState(checked)
    }

    fun share() {
        sharingInteractor.share()
    }

    fun support() {
        sharingInteractor.support()
    }

    fun terms() {
        sharingInteractor.terms()
    }
}