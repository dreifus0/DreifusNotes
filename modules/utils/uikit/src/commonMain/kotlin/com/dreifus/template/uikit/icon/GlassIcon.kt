package com.dreifus.template.uikit.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.glass.glassBorder
import com.dreifus.template.uikit.style.AppIcon
import com.dreifus.template.uikit.style.AppTheme

@Composable
fun GlassIcon(
    icon: AppIcon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color = AppTheme.colors.bgGlassPrimary, shape = CircleShape)
            .glassBorder(shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        icon(tint = AppTheme.colors.contentPrimary)
    }
}
