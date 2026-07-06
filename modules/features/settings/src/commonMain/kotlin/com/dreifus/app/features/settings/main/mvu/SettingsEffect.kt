package com.dreifus.app.features.settings.main.mvu

sealed interface SettingsEffect {
    data object DataReset : SettingsEffect
    data object NavigateToAppearance : SettingsEffect
    data object NavigateToNoteColors : SettingsEffect
    data class OpenUrl(val url: String) : SettingsEffect
}
