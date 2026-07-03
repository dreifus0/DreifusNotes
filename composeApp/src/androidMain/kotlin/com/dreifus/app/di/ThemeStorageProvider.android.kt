package com.dreifus.app.di

import android.content.Context
import com.dreifus.app.DreifusApplication
import com.dreifus.app.data.preferences.ThemeMode
import com.dreifus.app.data.preferences.ThemeStorage

private const val PREFS_NAME = "app_prefs"
private const val KEY_THEME_MODE = "theme_mode"

actual fun createThemeStorage(): ThemeStorage = AndroidThemeStorage()

private class AndroidThemeStorage : ThemeStorage {
    private val prefs =
        DreifusApplication.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun load(): ThemeMode = prefs.getString(KEY_THEME_MODE, null)
        ?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
        ?: ThemeMode.System

    override fun save(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }
}
