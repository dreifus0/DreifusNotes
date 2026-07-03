package com.dreifus.app.features.settings.main.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.app.data.preferences.ThemeMode

@Immutable
data class SettingsState(
    val isBiometricPending: Boolean = false,
    val showResetConfirmDialog: Boolean = false,
    val isResetting: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.System,
)
