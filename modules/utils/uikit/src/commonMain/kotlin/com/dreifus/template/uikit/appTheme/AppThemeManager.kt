package com.dreifus.template.uikit.appTheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

object AppThemeManager {
    @Composable
    fun isAppInDarkTheme(): Boolean = isSystemInDarkTheme()
}
