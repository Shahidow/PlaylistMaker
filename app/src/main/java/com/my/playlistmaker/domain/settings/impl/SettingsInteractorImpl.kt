package com.my.playlistmaker.domain.settings.impl

import com.my.playlistmaker.data.settings.SettingsRepository
import com.my.playlistmaker.domain.settings.SettingsInteractor

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {

    override fun getThemeState(): Boolean {
        return settingsRepository.getThemeState()
    }

    override fun setThemeState(darkThemeEnabled: Boolean) {
        settingsRepository.setThemeState(darkThemeEnabled)
    }
}