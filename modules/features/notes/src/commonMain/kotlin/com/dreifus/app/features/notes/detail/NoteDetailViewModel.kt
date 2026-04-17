package com.dreifus.app.features.notes.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.features.notes.detail.mvu.NoteDetailCommand
import com.dreifus.app.features.notes.detail.mvu.NoteDetailEffect
import com.dreifus.app.features.notes.detail.mvu.NoteDetailEvent
import com.dreifus.app.features.notes.detail.mvu.NoteDetailState
import com.dreifus.app.features.notes.detail.mvu.NoteDetailUpdate
import com.dreifus.app.features.notes.detail.mvu.commandHandler.NoteDetailDeleteBlockHandler
import com.dreifus.app.features.notes.detail.mvu.commandHandler.NoteDetailInitHandler
import com.dreifus.app.features.notes.detail.mvu.commandHandler.NoteDetailInsertBlockHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(NoteDetailViewModel::class)
@ContributesIntoMap(AppScope::class)
class NoteDetailViewModel(
    repository: NotesRepository,
) : ViewModel() {

    private val store = Store<NoteDetailState, NoteDetailEvent, NoteDetailEvent.Ui, NoteDetailCommand, NoteDetailEffect>(
        initialState = NoteDetailState(),
        update = NoteDetailUpdate,
        commandHandlers = listOf(
            NoteDetailInitHandler(repository),
            NoteDetailInsertBlockHandler(repository),
            NoteDetailDeleteBlockHandler(repository),
        ),
    )

    val state: StateFlow<NoteDetailState> = store.state
    val effects: Flow<NoteDetailEffect> = store.effects

    init {
        store.launch(viewModelScope)
    }

    fun dispatch(event: NoteDetailEvent.Ui) = store.dispatch(event)
}
