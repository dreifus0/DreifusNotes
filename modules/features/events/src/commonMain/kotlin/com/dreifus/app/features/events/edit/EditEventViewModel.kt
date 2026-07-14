package com.dreifus.app.features.events.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.EventsRepository
import com.dreifus.app.features.events.edit.mvu.EditEventCommand
import com.dreifus.app.features.events.edit.mvu.EditEventEffect
import com.dreifus.app.features.events.edit.mvu.EditEventEvent
import com.dreifus.app.features.events.edit.mvu.EditEventState
import com.dreifus.app.features.events.edit.mvu.EditEventUpdate
import com.dreifus.app.features.events.edit.mvu.commandHandler.EditEventDeleteHandler
import com.dreifus.app.features.events.edit.mvu.commandHandler.EditEventLoadHandler
import com.dreifus.app.features.events.edit.mvu.commandHandler.EditEventSaveHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@Inject
@ViewModelKey(EditEventViewModel::class)
@ContributesIntoMap(AppScope::class)
class EditEventViewModel(
    repository: EventsRepository,
) : ViewModel() {

    private val store =
        Store<EditEventState, EditEventEvent, EditEventEvent.Ui, EditEventCommand, EditEventEffect>(
            initialState = EditEventState(at = nextFullHour()),
            update = EditEventUpdate,
            commandHandlers = listOf(
                EditEventLoadHandler(repository),
                EditEventSaveHandler(repository),
                EditEventDeleteHandler(repository),
            ),
        )

    val state: StateFlow<EditEventState> = store.state
    val effects: Flow<EditEventEffect> = store.effects

    init {
        store.launch(viewModelScope)
    }

    fun dispatch(event: EditEventEvent.Ui) = store.dispatch(event)
}

/** Default moment for a new event: the upcoming full hour. */
private fun nextFullHour(): Long {
    val zone = TimeZone.currentSystemDefault()
    val now = Clock.System.now().toLocalDateTime(zone)
    val nextHour = LocalDateTime(now.year, now.month, now.dayOfMonth, now.hour, 0)
        .toInstant(zone)
        .plus(1, DateTimeUnit.HOUR)
    return nextHour.toEpochMilliseconds()
}
