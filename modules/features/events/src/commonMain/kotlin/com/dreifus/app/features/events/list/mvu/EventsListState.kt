package com.dreifus.app.features.events.list.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.template.uikit.style.NoteCardColor

@Immutable
data class EventsListState(
    val sections: List<EventSectionUi> = emptyList(),
    val isLoading: Boolean = true,
)

@Immutable
data class EventSectionUi(
    val label: String,
    val events: List<EventUiItem>,
)

@Immutable
data class EventUiItem(
    val id: Long,
    val title: String,
    val timeLabel: String,
    val color: NoteCardColor,
)
