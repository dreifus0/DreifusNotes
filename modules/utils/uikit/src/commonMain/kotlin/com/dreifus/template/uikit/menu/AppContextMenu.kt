package com.dreifus.template.uikit.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.dreifus.template.uikit.glass.glassBorder
import com.dreifus.template.uikit.icon.Edit24
import com.dreifus.template.uikit.icon.Lock24
import com.dreifus.template.uikit.icon.Palette24
import com.dreifus.template.uikit.icon.Trash24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcon
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme

data class ContextMenuItem(
    val label: String,
    val icon: AppIcon? = null,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit,
)

private val menuShape = RoundedCornerShape(14.dp)

@Composable
private fun AppContextMenu(
    items: List<ContextMenuItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .widthIn(min = 200.dp, max = 280.dp)
            .clip(menuShape)
            .background(AppTheme.colors.bgCardPrimary)
            .glassBorder(shape = menuShape),
    ) {
        items.forEachIndexed { index, item ->
            ContextMenuRow(item = item)
            if (index < items.lastIndex) {
                HorizontalDivider(
                    color = AppTheme.colors.contentBorder.copy(alpha = 0.6f),
                    thickness = 0.5.dp,
                )
            }
        }
    }
}

@Composable
private fun ContextMenuRow(item: ContextMenuItem) {
    val labelColor = if (item.isDestructive) AppTheme.colors.accentError else AppTheme.colors.contentPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = item.onClick,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.label,
            style = AppTheme.typography.bodyLarge,
            color = labelColor,
        )
        item.icon?.invoke(
            modifier = Modifier.size(18.dp),
            tint = labelColor,
        )
    }
}

@Composable
fun ContextMenuPopup(
    anchorPosition: IntOffset,
    anchorSize: IntSize,
    alignEnd: Boolean,
    onDismissRequest: () -> Unit,
    items: List<ContextMenuItem>,
) {
    val density = LocalDensity.current
    val gapPx = with(density) { 12.dp.roundToPx() }
    val positionProvider = remember(anchorPosition, anchorSize, alignEnd, gapPx) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                val x = if (alignEnd) {
                    (anchorPosition.x + anchorSize.width - popupContentSize.width).coerceAtLeast(0)
                } else {
                    anchorPosition.x.coerceAtMost(windowSize.width - popupContentSize.width)
                }
                val yAbove = anchorPosition.y - popupContentSize.height - gapPx
                val y = if (yAbove >= 0) yAbove else anchorPosition.y + anchorSize.height + gapPx
                return IntOffset(x, y)
            }
        }
    }
    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true),
    ) {
        AppContextMenu(items = items)
    }
}

@Preview
@Composable
private fun PreviewContextMenuNoteCard() {
    AppPreview {
        AppContextMenu(
            modifier = Modifier.padding(16.dp),
            items = listOf(
                ContextMenuItem("Lock note", AppIcons.Lock24, onClick = {}),
                ContextMenuItem("Change color", AppIcons.Palette24, onClick = {}),
                ContextMenuItem("Edit", AppIcons.Edit24, onClick = {}),
                ContextMenuItem("Delete", AppIcons.Trash24, isDestructive = true, onClick = {}),
            ),
        )
    }
}
