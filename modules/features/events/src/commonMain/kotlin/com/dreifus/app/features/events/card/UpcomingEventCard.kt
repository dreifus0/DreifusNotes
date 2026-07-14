package com.dreifus.app.features.events.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.glass.glassBorder
import com.dreifus.template.uikit.icon.Calendar24
import com.dreifus.template.uikit.icon.ChevronRight24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import dreifusnotes.modules.features.events.generated.resources.Res
import dreifusnotes.modules.features.events.generated.resources.event_upcoming_empty_subtitle
import dreifusnotes.modules.features.events.generated.resources.event_upcoming_empty_title
import dreifusnotes.modules.features.events.generated.resources.event_upcoming_label
import org.jetbrains.compose.resources.stringResource

/**
 * Compact banner with the nearest upcoming event; shown at the top of the notes list.
 * With a null [title] it renders the "no upcoming events" placeholder — the card is
 * always visible and always leads to the events screen.
 */
@Composable
fun UpcomingEventCard(
    title: String?,
    subtitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = AppTheme.shapes.card
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AppTheme.colors.bgCardPrimary)
            .glassBorder(shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.contentPrimary),
            contentAlignment = Alignment.Center,
        ) {
            AppIcons.Calendar24(
                modifier = Modifier.size(18.dp),
                tint = AppTheme.colors.backgroundBase,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.event_upcoming_label).uppercase(),
                style = AppTheme.typography.bodySmall.copy(
                    fontSize = AppTheme.typography.bodySmall.fontSize * 0.85f,
                ),
                color = AppTheme.colors.contentTertiary,
            )
            Text(
                text = title ?: stringResource(Res.string.event_upcoming_empty_title),
                style = AppTheme.typography.headlineMedium,
                color = AppTheme.colors.contentPrimary,
            )
            Text(
                text = subtitle ?: stringResource(Res.string.event_upcoming_empty_subtitle),
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.contentSecondary,
            )
        }
        AppIcons.ChevronRight24(tint = AppTheme.colors.contentTertiary)
    }
}

@Preview
@Composable
private fun PreviewUpcomingEventCard() {
    AppPreview {
        UpcomingEventCard(
            title = "Dentist appointment",
            subtitle = "Tomorrow · 10:00 AM",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewUpcomingEventCardEmpty() {
    AppPreview {
        UpcomingEventCard(
            title = null,
            subtitle = null,
            onClick = {},
        )
    }
}
