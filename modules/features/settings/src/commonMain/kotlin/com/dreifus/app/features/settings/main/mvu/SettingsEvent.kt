package com.dreifus.app.features.settings.main.mvu

import com.dreifus.app.data.preferences.ThemeMode

sealed interface SettingsEvent {
    sealed interface Ui : SettingsEvent {
        data object Init : Ui
        data object AppearanceClick : Ui
        data object NoteColorsClick : Ui
        data object ResetDataClick : Ui
        data object PrivacyPolicyClick : Ui
        data object BiometricSuccess : Ui
        data object BiometricDismissed : Ui
        data object BiometricUnavailable : Ui
        data object ResetConfirmed : Ui
        data object ResetConfirmDismissed : Ui
    }

    data object ResetComplete : SettingsEvent
    data class ThemeModeLoaded(val mode: ThemeMode) : SettingsEvent
}
