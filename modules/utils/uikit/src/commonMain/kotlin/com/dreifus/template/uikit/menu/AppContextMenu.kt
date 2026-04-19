package com.dreifus.template.uikit.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun AppContextMenu(
    items: List<ContextMenuItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .shadow(elevation = 12.dp, shape = menuShape)
            .clip(menuShape)
            .background(AppTheme.colors.backgroundSecondary),
    ) {
        items.forEachIndexed { index, item ->
            ContextMenuRow(item = item)
            if (index < items.lastIndex) {
                HorizontalDivider(
                    color = AppTheme.colors.contentDividers,
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
