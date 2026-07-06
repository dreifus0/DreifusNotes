package com.dreifus.app.features.notes.detail.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.template.uikit.style.NoteCardColor

@Immutable
data class NoteDetailState(
    val noteId: Long = 0L,
    val title: String = "",
    val description: String = "",
    val color: NoteCardColor = NoteCardColor.Default,
    val favoriteColors: List<NoteCardColor> = NoteCardColor.DefaultFavorites,
    val blocks: List<NoteBlockUiItem> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = true,
    val isBlocksLoading: Boolean = true,
    val isProtected: Boolean = false,
    val updatedAt: Long = 0L,
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
        val items: List<ChecklistItemUi>,
        override val time: String,
        override val dayHeader: String?,
    ) : NoteBlockUiItem
}

@Immutable
data class ChecklistItemUi(
    val text: String,
    val isChecked: Boolean = false,
)

// A checklist block is stored as plain text: first line is the title, each following line is an
// item, prefixed with "[x] " when checked. Lines without the prefix stay backward compatible.
private const val CHECKED_MARKER = "[x] "

internal fun parseChecklistItem(line: String): ChecklistItemUi =
    if (line.startsWith(CHECKED_MARKER)) ChecklistItemUi(line.removePrefix(CHECKED_MARKER), isChecked = true)
    else ChecklistItemUi(line)

internal fun NoteBlockUiItem.Checklist.encodeToBlockText(): String =
    title + "\n" + items.joinToString("\n") { item ->
        if (item.isChecked) "$CHECKED_MARKER${item.text}" else item.text
    }
