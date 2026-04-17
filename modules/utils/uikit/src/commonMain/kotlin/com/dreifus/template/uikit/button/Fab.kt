package com.dreifus.template.uikit.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.icon.Plus24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme

@Composable
fun Fab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colors = AppTheme.colors
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = colors.contentShadow,
                spotColor = colors.contentShadow
            )
            .size(size)
            .clip(CircleShape)
            .background(colors.accentPrimary, CircleShape)
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = colors.accentOnPrimary),
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

@Preview
@Composable
private fun PreviewFab() {
    AppPreview {
        Box(modifier = Modifier.padding(24.dp)) {
            Fab(onClick = {}) {
                AppIcons.Plus24(tint = AppTheme.colors.accentOnPrimary)
            }
        }
    }
}
