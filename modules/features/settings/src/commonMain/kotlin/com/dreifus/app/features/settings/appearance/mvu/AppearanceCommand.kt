package com.dreifus.app.features.settings.appearance.mvu

import com.dreifus.app.data.preferences.ThemeMode

sealed interface AppearanceCommand {
    data object Observe : AppearanceCommand
    data class SetThemeMode(val mode: ThemeMode) : AppearanceCommand
}
