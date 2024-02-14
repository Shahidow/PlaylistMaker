package com.my.playlistmaker.data.settings.impl

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.my.playlistmaker.app.App
import com.my.playlistmaker.app.SETTING_EXAMPLE_PREFERENCES
import com.my.playlistmaker.app.SWITCH_KEY
import com.my.playlistmaker.data.settings.SettingsRepository

class SettingsRepositoryImpl(
    private val context: Context,
    private val sharedPrefs: SharedPreferences
) : SettingsRepository {

    fun themeSwitcher(): Boolean {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    override fun getThemeState(): Boolean {
        return sharedPrefs.getBoolean(SWITCH_KEY, themeSwitcher())
    }

    override fun setThemeState(darkThemeEnabled: Boolean) {
        (context as App).switchTheme(darkThemeEnabled)
    }
}