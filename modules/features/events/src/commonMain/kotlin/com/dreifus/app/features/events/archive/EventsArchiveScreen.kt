package com.dreifus.app.features.events.archive

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.events.archive.mvu.ArchivedEventUi
import com.dreifus.app.features.events.archive.mvu.EventsArchiveEffect
import com.dreifus.app.features.events.archive.mvu.EventsArchiveEvent
import com.dreifus.app.features.events.archive.mvu.EventsArchiveState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.template.uikit.glass.glassBorder
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.icon.GlassIcon
import com.dreifus.template.uikit.icon.Trash24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.NoteCardColor
import com.dreifus.template.uikit.toolbar.AppToolbar
import dev.zacsweers.metrox.viewmodel.metroViewModel
import dreifusnotes.modules.features.events.generated.resources.Res
import dreifusnotes.modules.features.events.generated.resources.archive_empty
import dreifusnotes.modules.features.events.generated.resources.archive_title
import org.jetbrains.compose.resources.stringResource

class EventsArchiveScreen : RegularScreen {

    @Composable
    override fun Content() {
        val vm = metroViewModel<EventsArchiveViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val regularNav = Navigation.regular

        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    EventsArchiveEffect.NavigateBack -> regularNav.pop()
                }
            }
        }

        EventsArchiveContent(
            state = state,
            onEvent = vm::dispatch,
        )
    }
}

@Composable
private fun EventsArchiveContent(
    state: EventsArchiveState,
    onEvent: (EventsArchiveEvent.Ui) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppToolbar(
            title = stringResource(Res.string.archive_title),
            centerTitle = false,
            startBlock = {
                GlassIcon(
                    icon = AppIcons.ArrowLeft24,
                    onClick = { onEvent(EventsArchiveEvent.Ui.BackClick) },
                    size = 36.dp,
                    iconSize = 16.dp,
                )
            },
            endBlock = null,
        )

        if (!state.isLoading && state.items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.archive_empty),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.contentSecondary,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.items, key = { it.id }) { item ->
                    ArchivedEventRow(
                        item = item,
                        onDeleteClick = { onEvent(EventsArchiveEvent.Ui.DeleteClick(item.id)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ArchivedEventRow(
    item: ArchivedEventUi,
    onDeleteClick: () -> Unit,
) {
    val shape = AppTheme.shapes.group
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AppTheme.colors.bgCardPrimary)
            .glassBorder(shape = shape)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Archived events keep their dot but muted, so the list reads as inactive.
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(item.color.seed.copy(alpha = 0.5f)),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = AppTheme.typography.headlineMedium,
                color = AppTheme.colors.contentSecondary,
            )
            Text(
                text = item.whenLabel,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.contentTertiary,
            )
        }
        AppIcons.Trash24(
            tint = AppTheme.colors.contentTertiary,
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDeleteClick,
                ),
        )
    }
}

@Preview
@Composable
private fun PreviewEventsArchive() {
    AppPreview {
        EventsArchiveContent(
            state = EventsArchiveState(
                isLoading = false,
                items = listOf(
                    ArchivedEventUi(1, "Dentist appointment", "Apr 10 · 10:00 AM", NoteCardColor.Purple),
                    ArchivedEventUi(2, "Team offsite", "Mar 28 · 9:00 AM", NoteCardColor.Green),
                ),
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewEventsArchiveEmpty() {
    AppPreview {
        EventsArchiveContent(
            state = EventsArchiveState(isLoading = false),
            onEvent = {},
        )
    }
}
