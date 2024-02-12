package com.my.playlistmaker.domain.settings

interface SettingsInteractor {
    fun getThemeState(): Boolean
    fun setThemeState(darkThemeEnabled: Boolean)
}