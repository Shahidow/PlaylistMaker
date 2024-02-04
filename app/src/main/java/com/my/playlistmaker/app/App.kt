package com.my.playlistmaker.app

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val SETTING_EXAMPLE_PREFERENCES = "setting_example_preferences"
const val SWITCH_KEY = "key_for_switch"

class App : Application() {

    private var darkTheme = false
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(SETTING_EXAMPLE_PREFERENCES, Application.MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(SWITCH_KEY, darkTheme)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs = getSharedPreferences(SETTING_EXAMPLE_PREFERENCES, Application.MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(SWITCH_KEY, darkTheme)
            .apply()
    }
}