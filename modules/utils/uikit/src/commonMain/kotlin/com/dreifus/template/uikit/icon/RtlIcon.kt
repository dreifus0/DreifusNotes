package com.dreifus.template.uikit.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun RtlIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
            modifier.scale(scaleX = -1f, scaleY = 1f)
        } else {
            modifier
        },
    )
}
