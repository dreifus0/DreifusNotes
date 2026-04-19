package com.dreifus.app.features.notes.detail.mvu

sealed interface NoteDetailCommand {
    data class Init(val noteId: Long) : NoteDetailCommand
    data class InsertBlock(val noteId: Long, val text: String) : NoteDetailCommand
    data class InsertPhotoBlock(val noteId: Long, val uri: String) : NoteDetailCommand
    data class InsertChecklistBlock(val noteId: Long, val title: String, val items: List<String>) : NoteDetailCommand
    data class DeleteBlock(val blockId: Long) : NoteDetailCommand
}
