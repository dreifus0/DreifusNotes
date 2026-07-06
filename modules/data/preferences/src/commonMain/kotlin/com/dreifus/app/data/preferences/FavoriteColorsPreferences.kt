package com.dreifus.app.data.preferences

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * App-wide, observable source of truth for the user's favorite note colors, stored as enum names.
 * An empty list means the user hasn't picked favorites yet — consumers fall back to defaults.
 */
interface FavoriteColorsPreferences {
    val favorites: StateFlow<List<String>>
    fun setFavorites(colors: List<String>)
}

class DefaultFavoriteColorsPreferences(
    private val storage: FavoriteColorsStorage,
) : FavoriteColorsPreferences {

    private val _favorites = MutableStateFlow(storage.load())
    override val favorites: StateFlow<List<String>> = _favorites.asStateFlow()

    override fun setFavorites(colors: List<String>) {
        if (_favorites.value == colors) return
        storage.save(colors)
        _favorites.value = colors
    }
}
