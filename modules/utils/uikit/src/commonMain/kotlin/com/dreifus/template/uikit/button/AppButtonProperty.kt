package com.dreifus.template.uikit.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.ThemedParam

@Immutable
class AppButtonProperty(
    val appearances: Appearances = Appearances(),
) {

    companion object : ThemedParam<AppButtonProperty>(AppButtonProperty())

    @Immutable
    class Appearance(
        internal val buttonBackgroundColor: Color,
        internal val contentColor: Color,
        internal val shouldDrawGlassBorder: Boolean = false,
        internal val drawStyle: DrawStyle = Fill,
        internal val brush: Brush? = null,
    ) {
        companion object {
            @get:Composable
            val primary: Appearance get() = AppTheme.themedParams[AppButtonProperty].appearances.primary
        }
    }

    open class Appearances {
        open val primary: Appearance
            @Composable
            get() = Appearance(
                buttonBackgroundColor = AppTheme.colors.accentPrimary,
                contentColor = AppTheme.colors.contentPrimary,
                shouldDrawGlassBorder = true,
            )
    }

    @Immutable
    sealed class ButtonSize(
        internal val height: Dp,
        internal val horizontalPadding: Dp,
        internal val verticalPadding: Dp,
    ) {

        data object Big : ButtonSize(
            height = 64.dp,
            horizontalPadding = 32.dp,
            verticalPadding = 22.dp,
        )

        data object Medium : ButtonSize(
            height = 56.dp,
            horizontalPadding = 32.dp,
            verticalPadding = 18.dp
        )
    }

    @Immutable
    sealed class IconSize(
        internal val size: Dp,
        internal val padding: Dp,
    ) {

        data object WrapContent : IconSize(
            size = 0.dp,
            padding = 8.dp,
        )

        data object Normal : IconSize(
            size = 24.dp,
            padding = 8.dp,
        )
    }
}
