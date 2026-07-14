package com.dreifus.app.features.events.list.mvu.commandHandler

import com.dreifus.app.data.notes.EventsRepository
import com.dreifus.app.data.notes.model.Event
import com.dreifus.app.features.events.format.EventDateFormat
import com.dreifus.app.features.events.list.mvu.EventSectionUi
import com.dreifus.app.features.events.list.mvu.EventUiItem
import com.dreifus.app.features.events.list.mvu.EventsListCommand
import com.dreifus.app.features.events.list.mvu.EventsListEvent
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class EventsListCommandHandler(
    private val repository: EventsRepository,
) : FilteringHandlerToFlow<EventsListCommand.ObserveUpcoming, EventsListCommand, EventsListEvent>(
    EventsListCommand.ObserveUpcoming::class,
    cancelPreviousOnNewCommand = true,
) {
    override suspend fun handleCommand(command: EventsListCommand.ObserveUpcoming): Flow<EventsListEvent> {
        val now = Clock.System.now().toEpochMilliseconds()
        return repository.observeUpcoming(now).map { events ->
            EventsListEvent.EventsLoaded(events.toSections())
        }
    }
}

/** Events arrive sorted by time, so grouping preserves the Today → Later section order. */
private fun List<Event>.toSections(): List<EventSectionUi> =
    groupBy { event -> event.sectionLabel() }
        .map { (label, events) ->
            EventSectionUi(
                label = label,
                events = events.map { event ->
                    EventUiItem(
                        id = event.id,
                        title = event.title,
                        timeLabel = event.rowTimeLabel(),
                        color = NoteCardColor.deserialize(event.color),
                    )
                },
            )
        }

private fun Event.sectionLabel(): String = when {
    EventDateFormat.isToday(at) -> "Today"
    EventDateFormat.isTomorrow(at) -> "Tomorrow"
    EventDateFormat.isWithinWeek(at) -> "This week"
    else -> "Later"
}

private fun Event.rowTimeLabel(): String = when {
    EventDateFormat.isToday(at) || EventDateFormat.isTomorrow(at) -> EventDateFormat.timeLabel(at)
    EventDateFormat.isWithinWeek(at) -> EventDateFormat.weekdayTimeLabel(at)
    else -> EventDateFormat.dateTimeLabel(at)
}
