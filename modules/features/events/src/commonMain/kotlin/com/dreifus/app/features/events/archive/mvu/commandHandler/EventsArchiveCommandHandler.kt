package com.dreifus.app.features.events.archive.mvu.commandHandler

import com.dreifus.app.data.notes.EventsRepository
import com.dreifus.app.features.events.archive.mvu.ArchivedEventUi
import com.dreifus.app.features.events.archive.mvu.EventsArchiveCommand
import com.dreifus.app.features.events.archive.mvu.EventsArchiveEvent
import com.dreifus.app.features.events.format.EventDateFormat
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class EventsArchiveCommandHandler(
    private val repository: EventsRepository,
) : FilteringHandlerToFlow<EventsArchiveCommand.ObservePast, EventsArchiveCommand, EventsArchiveEvent>(
    EventsArchiveCommand.ObservePast::class,
    cancelPreviousOnNewCommand = true,
) {
    override suspend fun handleCommand(command: EventsArchiveCommand.ObservePast): Flow<EventsArchiveEvent> {
        val now = Clock.System.now().toEpochMilliseconds()
        return repository.observePast(now).map { events ->
            EventsArchiveEvent.ArchiveLoaded(events.map { event ->
                ArchivedEventUi(
                    id = event.id,
                    title = event.title,
                    whenLabel = EventDateFormat.dateTimeLabel(event.at),
                    color = NoteCardColor.deserialize(event.color),
                )
            })
        }
    }
}

class EventsArchiveDeleteHandler(
    private val repository: EventsRepository,
) : FilteringHandlerToFlow<EventsArchiveCommand.DeleteEvent, EventsArchiveCommand, EventsArchiveEvent>(
    EventsArchiveCommand.DeleteEvent::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: EventsArchiveCommand.DeleteEvent): Flow<EventsArchiveEvent> =
        flow {
            repository.delete(command.id)
        }
}
