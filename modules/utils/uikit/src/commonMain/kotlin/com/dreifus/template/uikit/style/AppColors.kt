package com.dreifus.template.uikit.style

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

@Immutable
data class AppColors(
    // Content
    val contentPrimary: Color,
    val contentSecondary: Color,
    val contentDividers: Color,
    val contentBorder: Color,
    val contentShadow: Color,
    // Background
    val backgroundBase: Color,
    val backgroundSecondary: Color,
    val backgroundDisabled: Color,
    val backgroundActive: Color,
    // Accent
    val accentPrimary: Color,
    val accentSecondary: Color,
    val accentError: Color,
    val accentLink: Color,
    val accentOnPrimary: Color,
    val accentOnSecondary: Color,
) {

    val isDarkTheme = backgroundBase.luminance() < 0.5f

    fun toComposeColorScheme(): ColorScheme {
        return if (isDarkTheme) {
            darkColorScheme(
                primary = accentSecondary,
                onPrimary = accentOnSecondary,
                secondary = accentPrimary,
                onSecondary = accentOnPrimary,
                background = backgroundBase,
                onBackground = contentPrimary,
                surface = backgroundBase,
                onSurface = contentPrimary,
                error = accentError,
                onError = contentPrimary,
                outline = contentDividers,
                surfaceVariant = backgroundSecondary,
                primaryContainer = accentPrimary,
                onPrimaryContainer = backgroundBase,
            )
        } else {
            lightColorScheme(
                primary = accentPrimary,
                onPrimary = accentOnPrimary,
                secondary = accentSecondary,
                onSecondary = accentOnSecondary,
                background = backgroundBase,
                onBackground = contentPrimary,
                surface = backgroundBase,
                onSurface = contentPrimary,
                error = accentError,
                onError = contentPrimary,
                outline = contentDividers,
                surfaceVariant = backgroundSecondary,
                primaryContainer = accentPrimary,
                onPrimaryContainer = backgroundBase,
            )
        }
    }
}

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        contentPrimary = Color.Unspecified,
        contentSecondary = Color.Unspecified,
        contentDividers = Color.Unspecified,
        contentBorder = Color.Unspecified,
        contentShadow = Color.Unspecified,
        backgroundBase = Color.Unspecified,
        backgroundSecondary = Color.Unspecified,
        backgroundDisabled = Color.Unspecified,
        backgroundActive = Color.Unspecified,
        accentPrimary = Color.Unspecified,
        accentSecondary = Color.Unspecified,
        accentError = Color.Unspecified,
        accentLink = Color.Unspecified,
        accentOnPrimary = Color.Unspecified,
        accentOnSecondary = Color.Unspecified,
    )
}

