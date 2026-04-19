package com.dreifus.app.features.pin.lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.features.pin.lock.mvu.PinLockCommand
import com.dreifus.app.features.pin.lock.mvu.PinLockEffect
import com.dreifus.app.features.pin.lock.mvu.PinLockEvent
import com.dreifus.app.features.pin.lock.mvu.PinLockState
import com.dreifus.app.features.pin.lock.mvu.PinLockUpdate
import com.dreifus.app.features.pin.lock.mvu.commandHandler.PinLockVerifyPinHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(PinLockViewModel::class)
@ContributesIntoMap(AppScope::class)
class PinLockViewModel(
    repository: NotesRepository,
) : ViewModel() {

    private val store = Store<PinLockState, PinLockEvent, PinLockEvent.Ui, PinLockCommand, PinLockEffect>(
        initialState = PinLockState(),
        update = PinLockUpdate,
        commandHandlers = listOf(
            PinLockVerifyPinHandler(repository),
        ),
    )

    val state: StateFlow<PinLockState> = store.state
    val effects: Flow<PinLockEffect> = store.effects

    init {
        store.launch(viewModelScope)
    }

    fun dispatch(event: PinLockEvent.Ui) = store.dispatch(event)
}
