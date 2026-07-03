package com.dreifus.app.di

import com.dreifus.app.data.preferences.ThemeMode
import com.dreifus.app.data.preferences.ThemeStorage
import platform.Foundation.NSUserDefaults

private const val KEY_THEME_MODE = "theme_mode"

actual fun createThemeStorage(): ThemeStorage = IosThemeStorage()

private class IosThemeStorage : ThemeStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun load(): ThemeMode = defaults.stringForKey(KEY_THEME_MODE)
        ?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
        ?: ThemeMode.System

    override fun save(mode: ThemeMode) {
        defaults.setObject(mode.name, KEY_THEME_MODE)
    }
}
