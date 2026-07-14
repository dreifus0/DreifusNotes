package com.dreifus.app.features.events.archive.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.template.uikit.style.NoteCardColor

@Immutable
data class EventsArchiveState(
    val items: List<ArchivedEventUi> = emptyList(),
    val isLoading: Boolean = true,
)

@Immutable
data class ArchivedEventUi(
    val id: Long,
    val title: String,
    val whenLabel: String,
    val color: NoteCardColor,
)
