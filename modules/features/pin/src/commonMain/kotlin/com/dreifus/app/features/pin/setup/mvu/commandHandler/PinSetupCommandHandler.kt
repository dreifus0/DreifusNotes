package com.dreifus.app.features.pin.setup.mvu.commandHandler

import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.features.pin.setup.mvu.PinSetupCommand
import com.dreifus.app.features.pin.setup.mvu.PinSetupEvent
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PinSetupSavePinHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<PinSetupCommand.SavePin, PinSetupCommand, PinSetupEvent>(
    PinSetupCommand.SavePin::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: PinSetupCommand.SavePin): Flow<PinSetupEvent> = flow {
        repository.updatePin(command.noteId, command.pin)
        emit(PinSetupEvent.PinSaved)
    }
}
