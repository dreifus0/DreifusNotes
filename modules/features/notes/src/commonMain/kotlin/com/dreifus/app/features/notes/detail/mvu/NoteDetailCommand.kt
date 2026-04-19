package com.dreifus.app.features.notes.detail.mvu

sealed interface NoteDetailCommand {
    data class Init(val noteId: Long, val unlockedPin: String? = null) : NoteDetailCommand
    data class InsertBlock(val noteId: Long, val text: String, val pin: String? = null) : NoteDetailCommand
    data class InsertPhotoBlock(val noteId: Long, val uri: String, val pin: String? = null) : NoteDetailCommand
    data class InsertChecklistBlock(val noteId: Long, val title: String, val items: List<String>, val pin: String? = null) : NoteDetailCommand
    data class DeleteBlock(val blockId: Long) : NoteDetailCommand
}
