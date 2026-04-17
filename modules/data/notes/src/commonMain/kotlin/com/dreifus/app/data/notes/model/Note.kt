package com.dreifus.app.data.notes.model

data class Note(
    val id: Long,
    val title: String,
    val description: String,
    val color: NoteColor,
    val isProtected: Boolean,
    val encryptedBody: ByteArray?,
    val iv: ByteArray?,
    val salt: ByteArray?,
    val createdAt: Long,
    val updatedAt: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Note) return false
        return id == other.id && updatedAt == other.updatedAt
    }

    override fun hashCode(): Int = id.hashCode()
}

enum class NoteColor { Purple, Pink, Green, Orange }
