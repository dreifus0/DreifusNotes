package com.dreifus.app.features.notes.create.mvu.commandHandler

import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.data.notes.model.NoteColor
import com.dreifus.app.features.notes.create.mvu.CreateNoteCommand
import com.dreifus.app.features.notes.create.mvu.CreateNoteEvent
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CreateNoteCommandHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<CreateNoteCommand.CreateNote, CreateNoteCommand, CreateNoteEvent>(
    CreateNoteCommand.CreateNote::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: CreateNoteCommand.CreateNote): Flow<CreateNoteEvent> = flow {
        repository.insert(
            title = command.title,
            description = command.description,
            color = command.color.toNoteColor(),
            isProtected = false,
            encryptedBody = null,
            iv = null,
            salt = null,
        )
        emit(CreateNoteEvent.NoteCreated)
    }
}

private fun NoteCardColor.toNoteColor() = when (this) {
    NoteCardColor.Purple -> NoteColor.Purple
    NoteCardColor.Pink -> NoteColor.Pink
    NoteCardColor.Green -> NoteColor.Green
    NoteCardColor.Orange -> NoteColor.Orange
}
