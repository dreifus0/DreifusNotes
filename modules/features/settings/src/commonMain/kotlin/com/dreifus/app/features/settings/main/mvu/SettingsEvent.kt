package com.dreifus.app.features.settings.main.mvu

sealed interface SettingsEvent {
    sealed interface Ui : SettingsEvent {
        data object ResetDataClick : Ui
        data object PrivacyPolicyClick : Ui
        data object BiometricSuccess : Ui
        data object BiometricDismissed : Ui
    }

    data object ResetComplete : SettingsEvent
}
