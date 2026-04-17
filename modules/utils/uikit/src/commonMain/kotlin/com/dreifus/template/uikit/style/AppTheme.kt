package com.dreifus.template.uikit.style

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current

    val shapes = AppShapes

    val themedParams: ThemedParamsMap
        @Composable
        get() = LocalThemedParams.current
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UnusedPrivateMember")
@Composable
fun AppTheme(
    colors: AppColors,
    typography: AppTypography,
    themedParams: ThemedParamsMap = LocalThemedParams.current,
    icons: IconsProvider = LocalIcons.current,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = colors.toComposeColorScheme()

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalIcons provides icons,
        LocalThemedParams provides themedParams,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography.toComposeTypography(),
        ) {
            val rippleColor = colors.backgroundActive
            val rippleConfiguration = RippleConfiguration(
                color = rippleColor,
                rippleAlpha = RippleAlpha(
                    draggedAlpha = rippleColor.alpha,
                    focusedAlpha = rippleColor.alpha,
                    hoveredAlpha = rippleColor.alpha,
                    pressedAlpha = rippleColor.alpha,
                ),
            )
            CompositionLocalProvider(
                LocalRippleConfiguration provides rippleConfiguration,
                content = content,
            )
        }
    }
}
