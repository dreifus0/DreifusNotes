package com.dreifus.app.features.settings.main.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val SettingsUpdate = Update<SettingsState, SettingsEvent, SettingsCommand, SettingsEffect> { state, event ->
    when (event) {
        SettingsEvent.Ui.ResetDataClick -> Next(
            state = state.copy(isBiometricPending = true),
        )
        SettingsEvent.Ui.PrivacyPolicyClick -> Next(
            state = state,
            effect = SettingsEffect.OpenUrl("https://github.com/dreifus0/DreifusNotes/blob/main/PRIVACY_POLICY.md"),
        )
        SettingsEvent.Ui.BiometricSuccess -> Next(
            state = state.copy(isBiometricPending = false, isResetting = true),
            command = SettingsCommand.ResetAllData,
        )
        SettingsEvent.Ui.BiometricDismissed -> Next(
            state = state.copy(isBiometricPending = false),
        )
        SettingsEvent.ResetComplete -> Next(
            state = state.copy(isResetting = false),
            effect = SettingsEffect.DataReset,
        )
    }
}
