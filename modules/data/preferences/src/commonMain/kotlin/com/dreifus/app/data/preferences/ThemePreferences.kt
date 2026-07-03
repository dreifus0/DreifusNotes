package com.dreifus.app.data.preferences

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** App-wide, observable source of truth for the selected [ThemeMode]. */
interface ThemePreferences {
    val themeMode: StateFlow<ThemeMode>
    fun setThemeMode(mode: ThemeMode)
}

class DefaultThemePreferences(
    private val storage: ThemeStorage,
) : ThemePreferences {

    private val _themeMode = MutableStateFlow(storage.load())
    override val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    override fun setThemeMode(mode: ThemeMode) {
        if (_themeMode.value == mode) return
        storage.save(mode)
        _themeMode.value = mode
    }
}
