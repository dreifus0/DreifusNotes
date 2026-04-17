package com.dreifus.app.features.notes.main.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.template.uikit.style.NoteCardColor

@Immutable
data class NotesListState(
    val notes: List<NoteUiItem> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = true,
)

@Immutable
data class NoteUiItem(
    val id: Long,
    val title: String,
    val body: String,
    val date: String,
    val color: NoteCardColor,
    val isProtected: Boolean,
)
