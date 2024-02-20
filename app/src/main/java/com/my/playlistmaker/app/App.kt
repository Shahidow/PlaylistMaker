package com.my.playlistmaker.app

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.my.playlistmaker.di.appModule
import com.my.playlistmaker.di.dataModule
import com.my.playlistmaker.di.domainModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val SETTING_EXAMPLE_PREFERENCES = "SETTING_EXAMPLE_PREFERENCES"
const val SWITCH_KEY = "KEY_FOR_SWITCH"

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule))
        }
        val sharedPrefs: SharedPreferences = getKoin().get()
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
        val sharedPrefs: SharedPreferences = getKoin().get()
        sharedPrefs.edit()
            .putBoolean(SWITCH_KEY, darkTheme)
            .apply()
    }
}