package com.dreifus.app.features.settings.appearance.mvu

import com.dreifus.app.data.preferences.ThemeMode

sealed interface AppearanceEvent {
    sealed interface Ui : AppearanceEvent {
        data object Init : Ui
        data class Select(val mode: ThemeMode) : Ui
        data object BackClick : Ui
    }

    data class ThemeModeLoaded(val mode: ThemeMode) : AppearanceEvent
}
