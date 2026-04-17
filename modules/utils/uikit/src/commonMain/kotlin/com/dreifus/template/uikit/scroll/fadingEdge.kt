package com.dreifus.template.uikit.scroll

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Important to apply before horizontalScroll/verticalScroll modifier
fun Modifier.fadingEdge(
    scrollableState: ScrollableState,
    orientation: Orientation = Orientation.Vertical,
    length: Dp = 16.dp,
): Modifier {
    return graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            if (scrollableState.canScrollBackward) {
                val startRect = when (orientation) {
                    Orientation.Vertical -> Rect(0f, 0f, size.width, length.toPx())
                    Orientation.Horizontal -> Rect(0f, 0f, length.toPx(), size.height)
                }
                val startBrush = when (orientation) {
                    Orientation.Vertical -> Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to Color.Black,
                        startY = startRect.top,
                        endY = startRect.bottom,
                    )

                    Orientation.Horizontal -> Brush.horizontalGradient(
                        0f to Color.Transparent,
                        1f to Color.Black,
                        startX = startRect.left,
                        endX = startRect.right,
                    )
                }
                drawRect(
                    topLeft = startRect.topLeft,
                    size = startRect.size,
                    brush = startBrush,
                    blendMode = BlendMode.DstIn,
                )
            }
            if (scrollableState.canScrollForward) {
                val endRect = when (orientation) {
                    Orientation.Vertical -> Rect(0f, size.height - length.toPx(), size.width, size.height)
                    Orientation.Horizontal -> Rect(size.width - length.toPx(), 0f, size.width, size.height)
                }
                val endBrush = when (orientation) {
                    Orientation.Vertical -> Brush.verticalGradient(
                        0f to Color.Black,
                        1f to Color.Transparent,
                        startY = endRect.top,
                        endY = endRect.bottom,
                    )

                    Orientation.Horizontal -> Brush.horizontalGradient(
                        0f to Color.Black,
                        1f to Color.Transparent,
                        startX = endRect.left,
                        endX = endRect.right,
                    )
                }
                drawRect(
                    topLeft = endRect.topLeft,
                    size = endRect.size,
                    brush = endBrush,
                    blendMode = BlendMode.DstIn,
                )
            }
        }
}
