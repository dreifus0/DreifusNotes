package com.dreifus.app.features.events.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.events.archive.EventsArchiveScreen
import com.dreifus.app.features.events.edit.EditEventScreen
import com.dreifus.app.features.events.list.mvu.EventSectionUi
import com.dreifus.app.features.events.list.mvu.EventUiItem
import com.dreifus.app.features.events.list.mvu.EventsListEffect
import com.dreifus.app.features.events.list.mvu.EventsListEvent
import com.dreifus.app.features.events.list.mvu.EventsListState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.template.uikit.button.AppButton
import com.dreifus.template.uikit.glass.glassBorder
import com.dreifus.template.uikit.icon.Archive24
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.icon.ChevronRight24
import com.dreifus.template.uikit.icon.GlassIcon
import com.dreifus.template.uikit.icon.Plus24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.row.AppSectionLabel
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.NoteCardColor
import com.dreifus.template.uikit.toolbar.AppToolbar
import dev.zacsweers.metrox.viewmodel.metroViewModel
import dreifusnotes.modules.features.events.generated.resources.Res
import dreifusnotes.modules.features.events.generated.resources.events_add_button
import dreifusnotes.modules.features.events.generated.resources.events_empty
import dreifusnotes.modules.features.events.generated.resources.events_title
import org.jetbrains.compose.resources.stringResource

class EventsListScreen : RegularScreen {

    @Composable
    override fun Content() {
        val vm = metroViewModel<EventsListViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val regularNav = Navigation.regular
        val bottomSheetNav = Navigation.bottomSheet

        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    EventsListEffect.NavigateBack -> regularNav.pop()
                    EventsListEffect.NavigateToArchive -> regularNav.navigate(EventsArchiveScreen())
                    is EventsListEffect.NavigateToEdit -> bottomSheetNav.navigate(EditEventScreen(effect.id))
                }
            }
        }

        EventsListContent(
            state = state,
            onEvent = vm::dispatch,
        )
    }
}

@Composable
private fun EventsListContent(
    state: EventsListState,
    onEvent: (EventsListEvent.Ui) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppToolbar(
            title = stringResource(Res.string.events_title),
            centerTitle = false,
            startBlock = {
                GlassIcon(
                    icon = AppIcons.ArrowLeft24,
                    onClick = { onEvent(EventsListEvent.Ui.BackClick) },
                    size = 36.dp,
                    iconSize = 16.dp,
                )
            },
            endBlock = {
                GlassIcon(
                    icon = AppIcons.Archive24,
                    onClick = { onEvent(EventsListEvent.Ui.ArchiveClick) },
                    size = 36.dp,
                    iconSize = 16.dp,
                )
                Spacer(Modifier.width(6.dp))
                GlassIcon(
                    icon = AppIcons.Plus24,
                    onClick = { onEvent(EventsListEvent.Ui.AddClick) },
                    size = 36.dp,
                    iconSize = 16.dp,
                )
            },
        )

        if (!state.isLoading && state.sections.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.events_empty),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.contentSecondary,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.sections.forEach { section ->
                    item(key = "label_${section.label}") {
                        AppSectionLabel(section.label, modifier = Modifier.padding(top = 8.dp))
                    }
                    items(section.events, key = { it.id }) { event ->
                        EventRow(
                            event = event,
                            onClick = { onEvent(EventsListEvent.Ui.EventClick(event.id)) },
                        )
                    }
                }
            }
        }

        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp),
            text = stringResource(Res.string.events_add_button),
            onClick = { onEvent(EventsListEvent.Ui.AddClick) },
        )
    }
}

@Composable
private fun EventRow(
    event: EventUiItem,
    onClick: () -> Unit,
) {
    val shape = AppTheme.shapes.group
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AppTheme.colors.bgCardPrimary)
            .glassBorder(shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(event.color.seed),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.title,
                style = AppTheme.typography.headlineMedium,
                color = AppTheme.colors.contentPrimary,
            )
            Text(
                text = event.timeLabel,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.contentTertiary,
            )
        }
        AppIcons.ChevronRight24(tint = AppTheme.colors.contentTertiary)
    }
}

@Preview
@Composable
private fun PreviewEventsList() {
    AppPreview {
        EventsListContent(
            state = EventsListState(
                isLoading = false,
                sections = listOf(
                    EventSectionUi(
                        label = "Today",
                        events = listOf(
                            EventUiItem(1, "Dentist appointment", "10:00 AM", NoteCardColor.Purple),
                            EventUiItem(2, "Call with Sam", "3:30 PM", NoteCardColor.Green),
                        ),
                    ),
                    EventSectionUi(
                        label = "Tomorrow",
                        events = listOf(
                            EventUiItem(3, "Grocery run", "9:00 AM", NoteCardColor.Orange),
                        ),
                    ),
                    EventSectionUi(
                        label = "This week",
                        events = listOf(
                            EventUiItem(4, "Book club", "Thu · 7:00 PM", NoteCardColor.Pink),
                            EventUiItem(5, "Weekend trip departs", "Fri · 6:00 PM", NoteCardColor.Red),
                        ),
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewEventsListEmpty() {
    AppPreview {
        EventsListContent(
            state = EventsListState(isLoading = false),
            onEvent = {},
        )
    }
}
