package com.my.playlistmaker.app

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.my.playlistmaker.di.appModule
import com.my.playlistmaker.di.dataModule
import com.my.playlistmaker.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val SETTING_EXAMPLE_PREFERENCES = "SETTING_EXAMPLE_PREFERENCES"
const val SWITCH_KEY = "KEY_FOR_SWITCH"

class App : Application() {

    private var darkTheme = false
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(SETTING_EXAMPLE_PREFERENCES, Application.MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(SWITCH_KEY, darkTheme)
        switchTheme(darkTheme)
        startKoin{
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule))
        }
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