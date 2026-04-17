package com.dreifus.template.uikit.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.icon.Lock24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.NoteCardColor

@Composable
fun AppCard(
    title: String,
    body: String,
    date: String,
    color: NoteCardColor,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    onClick: () -> Unit = {},
) {
    val palette = color.palette()
    val shape = AppTheme.shapes.card

    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .clip(shape)
            .background(palette.background, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick,
            )
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (isLocked) {
                AppIcons.Lock24(tint = palette.title)
            }
            Text(
                text = title,
                style = AppTheme.typography.headlineSmall,
                color = palette.title,
                maxLines = 1,
            )
        }

        Text(
            text = body,
            style = AppTheme.typography.bodySmall,
            color = palette.body,
            maxLines = 2,
            modifier = if (isLocked) Modifier.blur(3.dp) else Modifier,
        )

        Spacer(2.dp)

        Text(
            text = date,
            style = AppTheme.typography.headlineSmall.copy(
                fontSize = AppTheme.typography.bodySmall.fontSize,
                letterSpacing = AppTheme.typography.headlineSmall.letterSpacing,
            ),
            color = palette.date,
        )
    }
}

@Preview
@Composable
private fun PreviewCards() {
    AppPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AppCard(
                title = "Project ideas",
                body = "Build a notes app with pin protection, maybe add tags later…",
                date = "TODAY",
                color = NoteCardColor.Purple,
            )
            AppCard(
                title = "Protected note",
                body = "Lorem ipsum dolor sit amet consectetur",
                date = "YESTERDAY",
                color = NoteCardColor.Pink,
                isLocked = true,
            )
            AppCard(
                title = "Grocery list",
                body = "Milk, bread, coffee beans, avocados, olive oil, tomatoes",
                date = "MON",
                color = NoteCardColor.Green,
            )
            AppCard(
                title = "Book notes",
                body = "Chapter 3 — key insight about habit formation cycles",
                date = "APR 12",
                color = NoteCardColor.Orange,
            )
        }
    }
}
