package com.dreifus.app.features.notes.detail.mvu.commandHandler

import com.dreifus.app.data.notes.NotesRepository
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
    override suspend fun handleCommand(command: NoteDetailCommand.Init): Flow<NoteDetailEvent> = flow {
        val note = repository.getById(command.noteId) ?: return@flow
        emit(NoteDetailEvent.NoteLoaded(note.title, note.color.toCardColor()))
        repository.observeBlocksForNote(command.noteId).collect { rawBlocks ->
            val zone = TimeZone.currentSystemDefault()
            emit(NoteDetailEvent.BlocksLoaded(rawBlocks.mapIndexed { index, block ->
                val date = Instant.fromEpochMilliseconds(block.createdAt).toLocalDateTime(zone).date
                val prevDate = if (index > 0) {
                    Instant.fromEpochMilliseconds(rawBlocks[index - 1].createdAt).toLocalDateTime(zone).date
                } else null
                NoteBlockUiItem(
                    id = block.id,
                    text = block.text,
                    time = block.createdAt.toTime(zone),
                    dayHeader = if (prevDate == null || prevDate != date) date.toDayHeader(zone) else null,
                )
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
    override suspend fun handleCommand(command: NoteDetailCommand.InsertBlock): Flow<NoteDetailEvent> = flow {
        repository.insertBlock(command.noteId, command.text)
        emit(NoteDetailEvent.BlockSent)
    }
}

class NoteDetailDeleteBlockHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NoteDetailCommand.DeleteBlock, NoteDetailCommand, NoteDetailEvent>(
    NoteDetailCommand.DeleteBlock::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteDetailCommand.DeleteBlock): Flow<NoteDetailEvent> = flow {
        repository.deleteBlock(command.blockId)
        emit(NoteDetailEvent.BlockDeleted)
    }
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
