package com.dreifus.app.features.notes.detail.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.template.uikit.style.NoteCardColor

@Immutable
data class NoteDetailState(
    val noteId: Long = 0L,
    val title: String = "",
    val color: NoteCardColor = NoteCardColor.Default,
    val blocks: List<NoteBlockUiItem> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = true,
)

@Immutable
data class NoteBlockUiItem(
    val id: Long,
    val text: String,
    val time: String,
    val dayHeader: String?,
)
