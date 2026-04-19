package com.dreifus.app.data.notes

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.dreifus.app.data.notes.db.DatabaseDriverFactory
import com.dreifus.app.data.notes.db.NoteBlockEntity
import com.dreifus.app.data.notes.db.NoteEntity
import com.dreifus.app.data.notes.db.NotesDatabase
import com.dreifus.app.data.notes.model.Note
import com.dreifus.app.data.notes.model.NoteBlock
import com.dreifus.app.data.notes.model.NoteBlockType
import com.dreifus.app.data.notes.model.NoteColor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

@Inject
@SingleIn(AppScope::class)
class NotesRepository(
    driverFactory: DatabaseDriverFactory,
) {

    private val db = NotesDatabase(driverFactory.create())
    private val noteQueries = db.noteQueries
    private val blockQueries = db.noteBlockQueries

    fun observeAll(): Flow<List<Note>> =
        noteQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(NoteEntity::toDomain) }

    fun search(query: String): Flow<List<Note>> =
        noteQueries.searchByTitleOrDescription(query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(NoteEntity::toDomain) }

    suspend fun getById(id: Long): Note? =
        noteQueries.selectById(id).executeAsOneOrNull()?.toDomain()

    suspend fun getBlocksForNote(noteId: Long): List<NoteBlock> =
        blockQueries.selectByNoteId(noteId).executeAsList().map(NoteBlockEntity::toDomain)

    fun observeBlocksForNote(noteId: Long): Flow<List<NoteBlock>> =
        blockQueries.selectByNoteId(noteId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(NoteBlockEntity::toDomain) }

    suspend fun insert(
        title: String,
        description: String,
        color: NoteColor,
        isProtected: Boolean,
        encryptedBody: ByteArray?,
        iv: ByteArray?,
        salt: ByteArray?,
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        noteQueries.insert(
            title = title,
            description = description,
            color = color.name,
            is_protected = if (isProtected) 1L else 0L,
            encrypted_body = encryptedBody,
            iv = iv,
            salt = salt,
            created_at = now,
            updated_at = now,
        )
    }

    suspend fun update(note: Note) {
        noteQueries.update(
            title = note.title,
            description = note.description,
            color = note.color.name,
            is_protected = if (note.isProtected) 1L else 0L,
            encrypted_body = note.encryptedBody,
            iv = note.iv,
            salt = note.salt,
            updated_at = Clock.System.now().toEpochMilliseconds(),
            id = note.id,
        )
    }

    suspend fun updatePin(id: Long, pin: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        val storedPin = if (pin.isEmpty()) "" else PinHasher.hash(pin)

        if (pin.isNotEmpty()) {
            val note = getById(id)
            val blocks = getBlocksForNote(id)

            val encDescription = note?.description?.takeIf { it.isNotEmpty() }
                ?.let { NoteEncryptor.encrypt(it, pin) }
            val encBlocks = blocks
                .filter { !it.text.startsWith("ENC:v1:") }
                .associate { it.id to NoteEncryptor.encrypt(it.text, pin).encodeToString() }

            db.transaction {
                if (encDescription != null) {
                    noteQueries.updateEncryptedBody(
                        description = "",
                        encrypted_body = encDescription.ciphertext,
                        iv = encDescription.iv,
                        salt = encDescription.salt,
                        updated_at = now,
                        id = id,
                    )
                }
                for ((blockId, encText) in encBlocks) {
                    blockQueries.updateText(text = encText, id = blockId)
                }
                noteQueries.updatePin(pin = storedPin, is_protected = 1L, updated_at = now, id = id)
            }
        } else {
            noteQueries.updatePin(pin = storedPin, is_protected = 0L, updated_at = now, id = id)
        }
    }

    suspend fun delete(id: Long) {
        noteQueries.delete(id)
    }

    suspend fun deleteAll() {
        noteQueries.deleteAll()
    }

    suspend fun insertBlock(noteId: Long, type: NoteBlockType = NoteBlockType.TEXT, text: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        blockQueries.insert(note_id = noteId, type = type.name, text = text, created_at = now)
        noteQueries.touchUpdatedAt(updated_at = now, id = noteId)
    }

    suspend fun updateBlockText(blockId: Long, text: String) {
        blockQueries.updateText(text = text, id = blockId)
    }

    suspend fun deleteBlock(blockId: Long) {
        blockQueries.delete(blockId)
    }
}

private fun NoteEntity.toDomain() = Note(
    id = id,
    title = title,
    description = description,
    color = runCatching { NoteColor.valueOf(color) }.getOrDefault(NoteColor.Purple),
    isProtected = is_protected != 0L,
    pin = pin,
    encryptedBody = encrypted_body,
    iv = iv,
    salt = salt,
    createdAt = created_at,
    updatedAt = updated_at,
)

private fun NoteBlockEntity.toDomain() = NoteBlock(
    id = id,
    noteId = note_id,
    type = runCatching { NoteBlockType.valueOf(type) }.getOrDefault(NoteBlockType.TEXT),
    text = text,
    createdAt = created_at,
)
