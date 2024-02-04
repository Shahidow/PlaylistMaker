package com.my.playlistmaker.data.settings

interface SettingsRepository {
    fun getThemeState(): Boolean
    fun setThemeState(darkThemeEnabled: Boolean)
}