package com.dreifus.app.features.settings.main.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val SettingsUpdate = Update<SettingsState, SettingsEvent, SettingsCommand, SettingsEffect> { state, event ->
    when (event) {
        SettingsEvent.Ui.Init -> Next(
            state = state,
            command = SettingsCommand.ObserveTheme,
        )
        SettingsEvent.Ui.AppearanceClick -> Next(
            state = state,
            effect = SettingsEffect.NavigateToAppearance,
        )
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
        SettingsEvent.Ui.BiometricUnavailable -> Next(
            state = state.copy(isBiometricPending = false, showResetConfirmDialog = true),
        )
        SettingsEvent.Ui.ResetConfirmed -> Next(
            state = state.copy(showResetConfirmDialog = false, isResetting = true),
            command = SettingsCommand.ResetAllData,
        )
        SettingsEvent.Ui.ResetConfirmDismissed -> Next(
            state = state.copy(showResetConfirmDialog = false),
        )
        SettingsEvent.ResetComplete -> Next(
            state = state.copy(isResetting = false),
            effect = SettingsEffect.DataReset,
        )
        is SettingsEvent.ThemeModeLoaded -> Next(
            state = state.copy(themeMode = event.mode),
        )
    }
}
