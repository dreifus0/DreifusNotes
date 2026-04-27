package com.dreifus.app.features.notes.detail.mvu.commandHandler

import com.dreifus.app.data.notes.NoteEncryptor
import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.data.notes.decodeEncryptedData
import com.dreifus.app.data.notes.encodeToString
import com.dreifus.app.data.notes.model.NoteBlockType
import com.dreifus.app.data.notes.model.NoteColor
import com.dreifus.app.features.notes.detail.mvu.NoteBlockUiItem
import com.dreifus.app.features.notes.detail.mvu.NoteDetailCommand
import com.dreifus.app.features.notes.detail.mvu.NoteDetailEvent
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

class NoteDetailInitHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.Init, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.Init::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.Init): Flow<NoteDetailEvent> =
        flow {
            val note = repository.getById(command.noteId) ?: return@flow
            emit(NoteDetailEvent.NoteLoaded(note.title, note.color.toCardColor()))
            repository.observeBlocksForNote(command.noteId).collect { rawBlocks ->
                val zone = TimeZone.currentSystemDefault()
                emit(NoteDetailEvent.BlocksLoaded(rawBlocks.mapIndexed { index, block ->
                    val date =
                        Instant.fromEpochMilliseconds(block.createdAt).toLocalDateTime(zone).date
                    val prevDate = if (index > 0) {
                        Instant.fromEpochMilliseconds(rawBlocks[index - 1].createdAt)
                            .toLocalDateTime(zone).date
                    } else null
                    val dayHeader =
                        if (prevDate == null || prevDate != date) date.toDayHeader(zone) else null
                    val time = block.createdAt.toTime(zone)
                    val text = block.text.decryptIfEncoded(command.unlockedPin)
                    when (block.type) {
                        NoteBlockType.TEXT -> NoteBlockUiItem.Text(block.id, text, time, dayHeader)
                        NoteBlockType.PHOTO -> NoteBlockUiItem.Photo(
                            block.id,
                            text,
                            time,
                            dayHeader
                        )

                        NoteBlockType.CHECKLIST -> {
                            val lines = text.lines()
                            NoteBlockUiItem.Checklist(
                                id = block.id,
                                title = lines.firstOrNull().orEmpty(),
                                items = lines.drop(1).filter(String::isNotBlank),
                                time = time,
                                dayHeader = dayHeader,
                            )
                        }
                    }
                }))
            }
        }
}

class NoteDetailInsertBlockHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.InsertBlock, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.InsertBlock::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.InsertBlock): Flow<NoteDetailEvent> =
        flow {
            val text = command.pin?.let { NoteEncryptor.encrypt(command.text, it).encodeToString() }
                ?: command.text
            repository.insertBlock(command.noteId, text = text)
            emit(NoteDetailEvent.BlockSent)
        }
}

class NoteDetailInsertPhotoBlockHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.InsertPhotoBlock, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.InsertPhotoBlock::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.InsertPhotoBlock): Flow<NoteDetailEvent> =
        flow {
            val uri = command.pin?.let { NoteEncryptor.encrypt(command.uri, it).encodeToString() }
                ?: command.uri
            repository.insertBlock(command.noteId, NoteBlockType.PHOTO, uri)
            emit(NoteDetailEvent.BlockSent)
        }
}

class NoteDetailInsertChecklistBlockHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.InsertChecklistBlock, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.InsertChecklistBlock::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.InsertChecklistBlock): Flow<NoteDetailEvent> =
        flow {
            val raw = "${command.title}\n${command.items.joinToString("\n")}"
            val text = command.pin?.let { NoteEncryptor.encrypt(raw, it).encodeToString() } ?: raw
            repository.insertBlock(command.noteId, NoteBlockType.CHECKLIST, text)
            emit(NoteDetailEvent.BlockSent)
        }
}

class NoteDetailDeleteBlockHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.DeleteBlock, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.DeleteBlock::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.DeleteBlock): Flow<NoteDetailEvent> =
        flow {
            repository.deleteBlock(command.blockId)
            emit(NoteDetailEvent.BlockDeleted)
        }
}

class NoteDetailUpdateBlockHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.UpdateBlock, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.UpdateBlock::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.UpdateBlock): Flow<NoteDetailEvent> =
        flow {
            val text = command.pin?.let { NoteEncryptor.encrypt(command.text, it).encodeToString() }
                ?: command.text
            repository.updateBlockText(command.blockId, text)
            emit(NoteDetailEvent.BlockUpdated)
        }
}

class NoteDetailRenameNoteHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.RenameNote, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.RenameNote::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.RenameNote): Flow<NoteDetailEvent> =
        flow {
            val note = repository.getById(command.noteId) ?: return@flow
            repository.update(note.copy(title = command.newTitle))
            emit(NoteDetailEvent.NoteRenamed)
        }
}

class NoteDetailChangeColorHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.ChangeNoteColor, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.ChangeNoteColor::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.ChangeNoteColor): Flow<NoteDetailEvent> =
        flow {
            val note = repository.getById(command.noteId) ?: return@flow
            repository.update(note.copy(color = command.color.toNoteColor()))
            emit(NoteDetailEvent.NoteColorChanged)
        }
}

class NoteDetailDeleteNoteHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.DeleteNote, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.DeleteNote::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.DeleteNote): Flow<NoteDetailEvent> =
        flow {
            repository.delete(command.noteId)
            emit(NoteDetailEvent.NoteDeleted)
        }
}

private fun NoteCardColor.toNoteColor() = when (this) {
    NoteCardColor.Purple -> NoteColor.Purple
    NoteCardColor.Pink -> NoteColor.Pink
    NoteCardColor.Green -> NoteColor.Green
    NoteCardColor.Orange -> NoteColor.Orange
}

private fun String.decryptIfEncoded(pin: String?): String {
    if (pin == null) return this
    val enc = decodeEncryptedData() ?: return this
    return NoteEncryptor.decrypt(enc.ciphertext, enc.iv, enc.salt, pin) ?: this
}

private fun NoteColor.toCardColor() = when (this) {
    NoteColor.Purple -> NoteCardColor.Purple
    NoteColor.Pink -> NoteCardColor.Pink
    NoteColor.Green -> NoteCardColor.Green
    NoteColor.Orange -> NoteCardColor.Orange
}

private fun Long.toTime(zone: TimeZone): String {
    val dt = Instant.fromEpochMilliseconds(this).toLocalDateTime(zone)
    return "${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}"
}

private fun LocalDate.toDayHeader(zone: TimeZone): String {
    val today = Clock.System.todayIn(zone)
    return when {
        this == today -> "TODAY"
        this == today.minus(1, DateTimeUnit.DAY) -> "YESTERDAY"
        this >= today.minus(6, DateTimeUnit.DAY) -> dayOfWeek.name
        else -> "${month.name.take(3)} $dayOfMonth"
    }
}
