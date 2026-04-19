package com.dreifus.app.features.settings.main.mvu

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsState(
    val isBiometricPending: Boolean = false,
    val isResetting: Boolean = false,
)
