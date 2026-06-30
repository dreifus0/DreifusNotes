package com.dreifus.app.features.notes.detail.mvu

import com.dreifus.template.uikit.style.NoteCardColor

sealed interface NoteDetailCommand {
    data class Init(val noteId: Long, val unlockedPin: String? = null) : NoteDetailCommand
    data class InsertBlock(val noteId: Long, val text: String, val pin: String? = null) : NoteDetailCommand
    data class InsertPhotoBlock(val noteId: Long, val uri: String, val pin: String? = null) : NoteDetailCommand
    data class InsertChecklistBlock(val noteId: Long, val title: String, val items: List<String>, val pin: String? = null) : NoteDetailCommand
    data class DeleteBlock(val blockId: Long) : NoteDetailCommand
    data class UpdateBlock(val blockId: Long, val text: String, val pin: String? = null) : NoteDetailCommand
    data class RenameNote(val noteId: Long, val newTitle: String) : NoteDetailCommand
    data class ChangeNoteColor(val noteId: Long, val color: NoteCardColor) : NoteDetailCommand
    data class UpdateDescription(val noteId: Long, val description: String, val pin: String? = null) : NoteDetailCommand
    data class RemoveProtection(val noteId: Long, val pin: String) : NoteDetailCommand
    data class DeleteNote(val noteId: Long) : NoteDetailCommand
}
