package com.dreifus.app.data.preferences

/** Platform-backed persistence for favorite note color names (SharedPreferences / NSUserDefaults). */
interface FavoriteColorsStorage {
    fun load(): List<String>
    fun save(colors: List<String>)
}
