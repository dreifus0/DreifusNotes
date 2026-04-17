package com.dreifus.template.uikit.style.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.dreifus.template.uikit.style.AppColors
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.AppTypography
import com.dreifus.template.uikit.style.FontFamily
import com.dreifus.template.uikit.style.ThemedParamsMap

abstract class SpecificAppTheme {

    protected abstract val lightColors: AppColors
    protected abstract val darkColors: AppColors
    protected open val themedParams: ThemedParamsMap = ThemedParamsMap()

    @Composable
    open operator fun invoke(
        darkTheme: Boolean,
        content: @Composable () -> Unit,
    ) {
        val colors = when {
            darkTheme -> darkColors
            else -> lightColors
        }

        CompositionLocalProvider(
            LocalSpecificAppTheme provides this,
        ) {
            AppTheme(
                colors = colors,
                typography = AppTypography(FontFamily),
                themedParams = themedParams,
                content = content
            )
        }
    }

    // Overridable Composable functions with default values are not currently supported
    @Composable
    operator fun invoke(
        content: @Composable () -> Unit,
    ) {
        invoke(darkTheme = isSystemInDarkTheme(), content = content)
    }

    companion object {
        /** Force light or dark theme, e.g. for camera screen */
        @Composable
        operator fun invoke(
            darkTheme: Boolean,
            content: @Composable () -> Unit,
        ) {
            LocalSpecificAppTheme.current.invoke(darkTheme = darkTheme, content = content)
        }
    }
}

val LocalSpecificAppTheme = staticCompositionLocalOf<SpecificAppTheme> { DefaultAppTheme }
