package com.dreifus.app.features.settings.appearance.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.app.data.preferences.ThemeMode

@Immutable
data class AppearanceState(
    val selected: ThemeMode = ThemeMode.System,
)
