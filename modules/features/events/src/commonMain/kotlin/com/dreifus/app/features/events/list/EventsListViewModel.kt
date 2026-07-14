package com.dreifus.app.features.events.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.EventsRepository
import com.dreifus.app.features.events.list.mvu.EventsListCommand
import com.dreifus.app.features.events.list.mvu.EventsListEffect
import com.dreifus.app.features.events.list.mvu.EventsListEvent
import com.dreifus.app.features.events.list.mvu.EventsListState
import com.dreifus.app.features.events.list.mvu.EventsListUpdate
import com.dreifus.app.features.events.list.mvu.commandHandler.EventsListCommandHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(EventsListViewModel::class)
@ContributesIntoMap(AppScope::class)
class EventsListViewModel(
    repository: EventsRepository,
) : ViewModel() {

    private val store =
        Store<EventsListState, EventsListEvent, EventsListEvent.Ui, EventsListCommand, EventsListEffect>(
            initialState = EventsListState(),
            update = EventsListUpdate,
            commandHandlers = listOf(EventsListCommandHandler(repository)),
        )

    val state: StateFlow<EventsListState> = store.state
    val effects: Flow<EventsListEffect> = store.effects

    init {
        store.launch(viewModelScope)
        store.dispatch(EventsListEvent.Ui.Init)
    }

    fun dispatch(event: EventsListEvent.Ui) = store.dispatch(event)
}
