@file:Suppress("NOTHING_TO_INLINE")

package com.dreifus.template.uikit.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

@Composable
inline fun Painter.wrapWith(
    size: Dp,
    tint: Color = Color.Unspecified,
    alpha: Float = 1f,
    scale: Float = 1f,
    rotate: Float = 0f,
) = wrapWith(DpSize(size, size), tint, alpha, scale, rotate)

@Composable
fun Painter.wrapWith(
    size: DpSize = DpSize.Unspecified,
    tint: Color = Color.Unspecified,
    alpha: Float = 1f,
    scale: Float = 1f,
    rotate: Float = 0f,
): Painter {
    @Suppress("ComplexCondition")
    return if (size == DpSize.Unspecified && tint == Color.Unspecified && alpha == 1f && rotate == 0f) {
        this
    } else {
        val density = LocalDensity.current
        remember(size, tint, alpha, scale, density) {
            PainterWrapper(
                origin = this,
                defaultSize = with(density) { size.toSize().takeIf { it != Size.Unspecified } },
                defaultColorFilter = tint.takeIf { it != Color.Unspecified }?.let { ColorFilter.tint(it) },
                extraAlpha = alpha,
                extraScale = scale,
                rotate = rotate,
            )
        }
    }
}

private class PainterWrapper(
    origin: Painter,
    defaultSize: Size? = null,
    defaultColorFilter: ColorFilter? = null,
    extraAlpha: Float = 1f,
    extraScale: Float = 1f,
    private val rotate: Float = 0f,
) : Painter() {
    private val origin: Painter = if (origin is PainterWrapper) origin.origin else origin
    private val defaultSize: Size? = defaultSize ?: if (origin is PainterWrapper) origin.intrinsicSize else null
    private val defaultColorFilter: ColorFilter? =
        defaultColorFilter ?: if (origin is PainterWrapper) origin.defaultColorFilter else null
    private val extraAlpha: Float = if (origin is PainterWrapper) origin.extraAlpha * extraAlpha else extraAlpha
    private val extraScale: Float = if (origin is PainterWrapper) origin.extraScale * extraScale else extraScale

    private var currentAlpha: Float = DefaultAlpha
    private var currentColorFilter: ColorFilter? = null

    override val intrinsicSize: Size
        get() = defaultSize ?: (origin.intrinsicSize * extraScale)

    override fun DrawScope.onDraw() {
        scale(extraScale) {
            rotate(rotate) {
                with(origin) {
                    draw(size, currentAlpha * extraAlpha, currentColorFilter ?: defaultColorFilter)
                }
            }
        }
    }

    override fun applyAlpha(alpha: Float): Boolean {
        currentAlpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        currentColorFilter = colorFilter
        return true
    }
}
