package com.dreifus.template.uikit.style

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import kotlin.properties.ReadOnlyProperty

object AppIcons

@Stable
class AppIcon(
    private val iconName: String,
    private val provider: @Composable () -> Painter,
) {

    val painter @Composable get() = LocalIcons.current.getOrPut(iconName) { provider() }

    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        tint: Color = Color.Unspecified,
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = modifier,
            tint = tint,
        )
    }
}

class IconsProvider(
    private val overrides: Map<String, @Composable () -> Painter> = emptyMap(),
) {

    @Composable
    fun getOrPut(iconName: String, builder: @Composable () -> Painter): Painter {
        return overrides[iconName]?.invoke() ?: builder()
    }
}

val LocalIcons = staticCompositionLocalOf {
    IconsProvider()
}

fun cachedIcon(
    builder: @Composable () -> Painter,
) = ReadOnlyProperty<AppIcons, AppIcon> { _, property ->
    AppIcon(property.name) {
        builder()
    }
}
