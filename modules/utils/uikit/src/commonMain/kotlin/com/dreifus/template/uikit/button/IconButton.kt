package com.dreifus.template.uikit.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.icon.Edit24
import com.dreifus.template.uikit.icon.Search24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme

enum class IconButtonStyle { Ghost, Surface, Primary }

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: IconButtonStyle = IconButtonStyle.Ghost,
    size: Dp = 40.dp,
    shape: Shape = CircleShape,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colors = AppTheme.colors
    val background = when (style) {
        IconButtonStyle.Ghost -> Color.Transparent
        IconButtonStyle.Surface -> colors.backgroundSecondary
        IconButtonStyle.Primary -> colors.accentPrimary
    }
    val border: BorderStroke? = when (style) {
        IconButtonStyle.Surface -> BorderStroke(0.5.dp, colors.contentBorder)
        else -> null
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(background, shape)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

@Preview
@Composable
private fun PreviewIconButton() {
    AppPreview {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {}, style = IconButtonStyle.Ghost) { AppIcons.ArrowLeft24() }
            IconButton(onClick = {}, style = IconButtonStyle.Surface) { AppIcons.Search24() }
            IconButton(onClick = {}, style = IconButtonStyle.Primary) {
                AppIcons.Edit24(tint = AppTheme.colors.accentOnPrimary)
            }
        }
    }
}
