package com.dreifus.template.uikit.toolbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.button.IconButton
import com.dreifus.template.uikit.button.IconButtonStyle
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.icon.Lock24
import com.dreifus.template.uikit.icon.MoreHoriz24
import com.dreifus.template.uikit.icon.Plus24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 11.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (navigationIcon != null) {
            navigationIcon()
        }

        if (title != null) {
            Text(
                text = title,
                style = AppTheme.typography.heading5,
                color = AppTheme.colors.contentPrimary,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = if (navigationIcon != null) 8.dp else 3.dp),
            )
        } else {
            Row(modifier = Modifier.weight(1f)) {}
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}

@Preview
@Composable
private fun PreviewTopBarNotesList() {
    AppPreview {
        TopBar(
            title = "Notes",
            actions = {
                IconButton(onClick = {}, style = IconButtonStyle.Surface) {
                    AppIcons.Plus24()
                }
            },
        )
    }
}

@Preview
@Composable
private fun PreviewTopBarNoteDetail() {
    AppPreview {
        TopBar(
            navigationIcon = {
                IconButton(onClick = {}, style = IconButtonStyle.Surface) {
                    AppIcons.ArrowLeft24()
                }
            },
            actions = {
                IconButton(onClick = {}, style = IconButtonStyle.Surface) {
                    AppIcons.Lock24()
                }
                IconButton(onClick = {}, style = IconButtonStyle.Surface) {
                    AppIcons.MoreHoriz24()
                }
            },
        )
    }
}
