package com.dreifus.app.features.events.edit.mvu.commandHandler

import com.dreifus.app.data.notes.EventsRepository
import com.dreifus.app.features.events.edit.mvu.EditEventCommand
import com.dreifus.app.features.events.edit.mvu.EditEventEvent
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EditEventLoadHandler(
    private val repository: EventsRepository,
) : FilteringHandlerToFlow<EditEventCommand.LoadEvent, EditEventCommand, EditEventEvent>(
    EditEventCommand.LoadEvent::class,
    cancelPreviousOnNewCommand = true,
) {
    override suspend fun handleCommand(command: EditEventCommand.LoadEvent): Flow<EditEventEvent> = flow {
        repository.getById(command.id)?.let { emit(EditEventEvent.EventLoaded(it)) }
    }
}

class EditEventSaveHandler(
    private val repository: EventsRepository,
) : FilteringHandlerToFlow<EditEventCommand.SaveEvent, EditEventCommand, EditEventEvent>(
    EditEventCommand.SaveEvent::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: EditEventCommand.SaveEvent): Flow<EditEventEvent> = flow {
        if (command.id == null) {
            repository.insert(
                title = command.title,
                at = command.at,
                color = presetColors.random().serialize(),
            )
        } else {
            repository.update(id = command.id, title = command.title, at = command.at)
        }
        emit(EditEventEvent.Saved)
    }

    private companion object {
        // Each event gets a random accent dot; the color is not user-editable for now.
        val presetColors = listOf(
            NoteCardColor.Purple,
            NoteCardColor.Pink,
            NoteCardColor.Green,
            NoteCardColor.Orange,
            NoteCardColor.Blue,
            NoteCardColor.Teal,
            NoteCardColor.Red,
            NoteCardColor.Yellow,
        )
    }
}

class EditEventDeleteHandler(
    private val repository: EventsRepository,
) : FilteringHandlerToFlow<EditEventCommand.DeleteEvent, EditEventCommand, EditEventEvent>(
    EditEventCommand.DeleteEvent::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: EditEventCommand.DeleteEvent): Flow<EditEventEvent> = flow {
        repository.delete(command.id)
        emit(EditEventEvent.Deleted)
    }
}
