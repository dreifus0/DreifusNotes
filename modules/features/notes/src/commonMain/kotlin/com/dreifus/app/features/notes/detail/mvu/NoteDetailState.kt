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
    val unlockedPin: String? = null,
)

sealed interface NoteBlockUiItem {
    val id: Long
    val dayHeader: String?
    val time: String

    @Immutable
    data class Text(
        override val id: Long,
        val text: String,
        override val time: String,
        override val dayHeader: String?,
    ) : NoteBlockUiItem

    @Immutable
    data class Photo(
        override val id: Long,
        val uri: String,
        override val time: String,
        override val dayHeader: String?,
    ) : NoteBlockUiItem

    @Immutable
    data class Checklist(
        override val id: Long,
        val title: String,
        val items: List<String>,
        override val time: String,
        override val dayHeader: String?,
    ) : NoteBlockUiItem
}
