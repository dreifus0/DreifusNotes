package com.dreifus.app.features.events.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.EventsRepository
import com.dreifus.app.features.events.archive.mvu.EventsArchiveCommand
import com.dreifus.app.features.events.archive.mvu.EventsArchiveEffect
import com.dreifus.app.features.events.archive.mvu.EventsArchiveEvent
import com.dreifus.app.features.events.archive.mvu.EventsArchiveState
import com.dreifus.app.features.events.archive.mvu.EventsArchiveUpdate
import com.dreifus.app.features.events.archive.mvu.commandHandler.EventsArchiveCommandHandler
import com.dreifus.app.features.events.archive.mvu.commandHandler.EventsArchiveDeleteHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(EventsArchiveViewModel::class)
@ContributesIntoMap(AppScope::class)
class EventsArchiveViewModel(
    repository: EventsRepository,
) : ViewModel() {

    private val store =
        Store<EventsArchiveState, EventsArchiveEvent, EventsArchiveEvent.Ui, EventsArchiveCommand, EventsArchiveEffect>(
            initialState = EventsArchiveState(),
            update = EventsArchiveUpdate,
            commandHandlers = listOf(
                EventsArchiveCommandHandler(repository),
                EventsArchiveDeleteHandler(repository),
            ),
        )

    val state: StateFlow<EventsArchiveState> = store.state
    val effects: Flow<EventsArchiveEffect> = store.effects

    init {
        store.launch(viewModelScope)
        store.dispatch(EventsArchiveEvent.Ui.Init)
    }

    fun dispatch(event: EventsArchiveEvent.Ui) = store.dispatch(event)
}
