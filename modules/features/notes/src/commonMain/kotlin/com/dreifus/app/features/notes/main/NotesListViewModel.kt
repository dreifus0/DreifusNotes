package com.dreifus.app.features.notes.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.features.notes.navigation.PinNavigation
import com.dreifus.app.features.notes.main.mvu.NotesListCommand
import com.dreifus.app.features.notes.main.mvu.NotesListEffect
import com.dreifus.app.features.notes.main.mvu.NotesListEvent
import com.dreifus.app.features.notes.main.mvu.NotesListState
import com.dreifus.app.features.notes.main.mvu.NotesListUpdate
import com.dreifus.app.features.notes.main.mvu.commandHandler.NotesListCommandHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(NotesListViewModel::class)
@ContributesIntoMap(AppScope::class)
class NotesListViewModel(
    repository: NotesRepository,
    val pinNavigation: PinNavigation,
) : ViewModel() {

    private val store =
        Store<NotesListState, NotesListEvent, NotesListEvent.Ui, NotesListCommand, NotesListEffect>(
            initialState = NotesListState(),
            update = NotesListUpdate,
            commandHandlers = listOf(NotesListCommandHandler(repository)),
        )

    val state: StateFlow<NotesListState> = store.state
    val effects: Flow<NotesListEffect> = store.effects

    init {
        store.launch(viewModelScope)
        store.dispatch(NotesListEvent.Ui.QueryChanged(""))
    }

    fun dispatch(event: NotesListEvent.Ui) = store.dispatch(event)
}
