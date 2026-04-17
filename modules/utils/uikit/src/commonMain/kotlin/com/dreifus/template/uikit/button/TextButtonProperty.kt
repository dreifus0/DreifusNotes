package com.dreifus.template.uikit.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.AppTypography

@Immutable
object TextButtonProperty {

    @Immutable
    class ColorSet private constructor(
        internal val buttonBackgroundColor: Color,
        internal val contentColor: Color,
        internal val loaderColor: Color,
    ) {
        companion object {
            val flat: ColorSet
                @Composable
                get() = flat()

            @Composable
            fun flat(contentColor: Color = AppTheme.colors.contentTertiary): ColorSet = ColorSet(
                buttonBackgroundColor = Color.Transparent,
                contentColor = contentColor,
                loaderColor = AppTheme.colors.contentPrimary
            )
        }
    }

    @Immutable
    sealed class Appearance(
        internal val minHeight: Dp,
        internal val style: AppTypography.() -> TextStyle,
    ) {
        data object Default : Appearance(
            minHeight = 20.dp,
            style = AppTypography::headlineLarge,
        )
    }

    @Immutable
    sealed class IconSize(
        internal val size: Dp,
        internal val paddingEnd: Dp,
    ) {
        data object WrapContent : IconSize(
            size = 0.dp,
            paddingEnd = 8.dp,
        )

        data object Normal : IconSize(
            size = 24.dp,
            paddingEnd = 8.dp,
        )
    }
}
