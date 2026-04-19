package com.dreifus.app.features.settings.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.features.settings.main.mvu.SettingsCommand
import com.dreifus.app.features.settings.main.mvu.SettingsEffect
import com.dreifus.app.features.settings.main.mvu.SettingsEvent
import com.dreifus.app.features.settings.main.mvu.SettingsState
import com.dreifus.app.features.settings.main.mvu.SettingsUpdate
import com.dreifus.app.features.settings.main.mvu.commandHandler.SettingsCommandHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(SettingsViewModel::class)
@ContributesIntoMap(AppScope::class)
class SettingsViewModel(
    repository: NotesRepository,
) : ViewModel() {

    private val store =
        Store<SettingsState, SettingsEvent, SettingsEvent.Ui, SettingsCommand, SettingsEffect>(
            initialState = SettingsState(),
            update = SettingsUpdate,
            commandHandlers = listOf(SettingsCommandHandler(repository)),
        )

    val state: StateFlow<SettingsState> = store.state
    val effects: Flow<SettingsEffect> = store.effects

    init {
        store.launch(viewModelScope)
    }

    fun dispatch(event: SettingsEvent.Ui) = store.dispatch(event)
}
