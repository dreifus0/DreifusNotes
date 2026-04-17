package com.dreifus.app.data.notes.model

data class NoteBlock(
    val id: Long,
    val noteId: Long,
    val text: String,
    val createdAt: Long,
)
