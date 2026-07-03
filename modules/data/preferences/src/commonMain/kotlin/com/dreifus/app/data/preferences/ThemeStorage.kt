package com.dreifus.app.data.preferences

/** Platform-backed persistence for the selected [ThemeMode] (SharedPreferences / NSUserDefaults). */
interface ThemeStorage {
    fun load(): ThemeMode
    fun save(mode: ThemeMode)
}
