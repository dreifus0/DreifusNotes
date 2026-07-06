package com.dreifus.app.di

import android.content.Context
import com.dreifus.app.DreifusApplication
import com.dreifus.app.data.preferences.FavoriteColorsStorage

private const val PREFS_NAME = "app_prefs"
private const val KEY_FAVORITE_COLORS = "favorite_colors"

actual fun createFavoriteColorsStorage(): FavoriteColorsStorage = AndroidFavoriteColorsStorage()

private class AndroidFavoriteColorsStorage : FavoriteColorsStorage {
    private val prefs =
        DreifusApplication.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun load(): List<String> = prefs.getString(KEY_FAVORITE_COLORS, null)
        ?.split(',')
        ?.filter(String::isNotBlank)
        .orEmpty()

    override fun save(colors: List<String>) {
        prefs.edit().putString(KEY_FAVORITE_COLORS, colors.joinToString(",")).apply()
    }
}
