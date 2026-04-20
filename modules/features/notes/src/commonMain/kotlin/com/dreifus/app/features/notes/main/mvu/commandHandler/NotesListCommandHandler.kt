package com.dreifus.app.features.notes.main.mvu.commandHandler

import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.data.notes.model.NoteColor
import com.dreifus.app.features.notes.main.mvu.NoteUiItem
import com.dreifus.app.features.notes.main.mvu.NotesListCommand
import com.dreifus.app.features.notes.main.mvu.NotesListEvent
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

class NotesListCommandHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<NotesListCommand.ObserveNotes, NotesListCommand, NotesListEvent>(
    NotesListCommand.ObserveNotes::class,
    cancelPreviousOnNewCommand = true,
) {
    override suspend fun handleCommand(command: NotesListCommand.ObserveNotes): Flow<NotesListEvent> {
        val notesFlow = if (command.query.isBlank()) {
            repository.observeAll()
        } else {
            repository.search(command.query)
        }
        return notesFlow.map { notes ->
            NotesListEvent.NotesLoaded(notes.map { note ->
                NoteUiItem(
                    id = note.id,
                    title = note.title,
                    body = if (note.isProtected) "•••••••••••••••••••••••••••••" else note.description,
                    date = note.updatedAt.toDisplayDate(),
                    color = note.color.toCardColor(),
                    isProtected = note.isProtected,
                )
            })
        }
    }
}

private fun NoteColor.toCardColor() = when (this) {
    NoteColor.Purple -> NoteCardColor.Purple
    NoteColor.Pink -> NoteCardColor.Pink
    NoteColor.Green -> NoteCardColor.Green
    NoteColor.Orange -> NoteCardColor.Orange
}

private fun Long.toDisplayDate(): String {
    val zone = TimeZone.currentSystemDefault()
    val date = Instant.fromEpochMilliseconds(this).toLocalDateTime(zone).date
    val today = Clock.System.todayIn(zone)
    return when (date) {
        today -> "TODAY"
        today.minus(1, DateTimeUnit.DAY) -> "YESTERDAY"
        else -> "${date.month.name.take(3)} ${date.dayOfMonth}"
    }
}
