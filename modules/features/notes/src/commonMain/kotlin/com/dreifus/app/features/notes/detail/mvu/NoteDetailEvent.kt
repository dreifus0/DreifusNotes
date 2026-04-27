package com.dreifus.app.features.notes.detail.mvu

import com.dreifus.template.uikit.style.NoteCardColor

sealed interface NoteDetailEvent {
    sealed interface Ui : NoteDetailEvent {
        data class Init(val noteId: Long, val unlockedPin: String? = null) : Ui
        data object BackClick : Ui
        data class InputChanged(val text: String) : Ui
        data object SendClick : Ui
        data class DeleteBlockClick(val blockId: Long) : Ui
        data class CopyBlockClick(val blockId: Long) : Ui
        data class EditBlockConfirmed(val blockId: Long, val newText: String) : Ui
        data object LockClick : Ui
        data object PhotoClick : Ui
        data object ChecklistClick : Ui
        data class PhotoSelected(val uri: String) : Ui
        data class ChecklistConfirmed(val title: String, val items: List<String>) : Ui
        data object ShareClick : Ui
        data class RenameConfirmed(val newTitle: String) : Ui
        data class ColorChangeConfirmed(val color: NoteCardColor) : Ui
        data object DeleteNoteConfirmed : Ui
    }

    data class NoteLoaded(val title: String, val color: NoteCardColor) : NoteDetailEvent
    data class BlocksLoaded(val blocks: List<NoteBlockUiItem>) : NoteDetailEvent
    data object BlockSent : NoteDetailEvent
    data object BlockDeleted : NoteDetailEvent
    data object BlockUpdated : NoteDetailEvent
    data object NoteRenamed : NoteDetailEvent
    data object NoteColorChanged : NoteDetailEvent
    data object NoteDeleted : NoteDetailEvent
}
