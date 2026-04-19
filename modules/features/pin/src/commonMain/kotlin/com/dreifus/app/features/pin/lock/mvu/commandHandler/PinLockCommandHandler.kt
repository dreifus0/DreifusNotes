package com.dreifus.app.features.pin.lock.mvu.commandHandler

import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.features.pin.lock.mvu.PinLockCommand
import com.dreifus.app.features.pin.lock.mvu.PinLockEvent
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PinLockVerifyPinHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<PinLockCommand.VerifyPin, PinLockCommand, PinLockEvent>(
    PinLockCommand.VerifyPin::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: PinLockCommand.VerifyPin): Flow<PinLockEvent> = flow {
        val note = repository.getById(command.noteId)
        if (note != null && note.pin == command.pin) {
            emit(PinLockEvent.PinVerified)
        } else {
            emit(PinLockEvent.PinRejected)
        }
    }
}
