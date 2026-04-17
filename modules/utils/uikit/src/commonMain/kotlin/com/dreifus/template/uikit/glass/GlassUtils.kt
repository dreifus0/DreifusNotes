package com.dreifus.template.uikit.glass

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

fun Modifier.glassBorder(shape: Shape = CircleShape) = this then Modifier.border(
    width = Dp.Hairline,
    brush = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = .8f),
            Color.White.copy(alpha = .2f),
        ),
    ),
    shape = shape,
)
