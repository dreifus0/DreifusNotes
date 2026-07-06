package com.dreifus.app.di

import com.dreifus.app.data.preferences.FavoriteColorsStorage
import platform.Foundation.NSUserDefaults

private const val KEY_FAVORITE_COLORS = "favorite_colors"

actual fun createFavoriteColorsStorage(): FavoriteColorsStorage = IosFavoriteColorsStorage()

private class IosFavoriteColorsStorage : FavoriteColorsStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun load(): List<String> = defaults.stringForKey(KEY_FAVORITE_COLORS)
        ?.split(',')
        ?.filter(String::isNotBlank)
        .orEmpty()

    override fun save(colors: List<String>) {
        defaults.setObject(colors.joinToString(","), KEY_FAVORITE_COLORS)
    }
}
