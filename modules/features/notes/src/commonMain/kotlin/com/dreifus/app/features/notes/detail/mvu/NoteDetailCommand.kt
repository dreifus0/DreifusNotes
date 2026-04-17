package com.dreifus.app.features.notes.detail.mvu

sealed interface NoteDetailCommand {
    data class Init(val noteId: Long) : NoteDetailCommand
    data class InsertBlock(val noteId: Long, val text: String) : NoteDetailCommand
    data class DeleteBlock(val blockId: Long) : NoteDetailCommand
}
