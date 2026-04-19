package com.dreifus.app.data.notes.model

data class NoteBlock(
    val id: Long,
    val noteId: Long,
    val type: NoteBlockType,
    val text: String,
    val createdAt: Long,
)

enum class NoteBlockType { TEXT, PHOTO, CHECKLIST }
