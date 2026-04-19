package com.dreifus.app.features.pin.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.features.pin.setup.mvu.PinSetupCommand
import com.dreifus.app.features.pin.setup.mvu.PinSetupEffect
import com.dreifus.app.features.pin.setup.mvu.PinSetupEvent
import com.dreifus.app.features.pin.setup.mvu.PinSetupState
import com.dreifus.app.features.pin.setup.mvu.PinSetupUpdate
import com.dreifus.app.features.pin.setup.mvu.commandHandler.PinSetupSavePinHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(PinSetupViewModel::class)
@ContributesIntoMap(AppScope::class)
class PinSetupViewModel(
    repository: NotesRepository,
) : ViewModel() {

    private val store = Store<PinSetupState, PinSetupEvent, PinSetupEvent.Ui, PinSetupCommand, PinSetupEffect>(
        initialState = PinSetupState(),
        update = PinSetupUpdate,
        commandHandlers = listOf(
            PinSetupSavePinHandler(repository),
        ),
    )

    val state: StateFlow<PinSetupState> = store.state
    val effects: Flow<PinSetupEffect> = store.effects

    init {
        store.launch(viewModelScope)
    }

    fun dispatch(event: PinSetupEvent.Ui) = store.dispatch(event)
}
