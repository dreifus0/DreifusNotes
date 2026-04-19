package com.dreifus.app.features.notes.pin.lock.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

private const val PIN_LENGTH = 4

val PinLockUpdate = Update<PinLockState, PinLockEvent, PinLockCommand, PinLockEffect> { state, event ->
    when (event) {
        is PinLockEvent.Ui.Init -> Next(state = state.copy(noteId = event.noteId))

        is PinLockEvent.Ui.KeyPress -> {
            if (state.enteredPin.length >= PIN_LENGTH) return@Update Next(state)
            val newPin = state.enteredPin + event.digit
            if (newPin.length == PIN_LENGTH) Next(
                state = state.copy(enteredPin = newPin, isError = false),
                command = PinLockCommand.VerifyPin(state.noteId, newPin),
            ) else Next(
                state = state.copy(enteredPin = newPin, isError = false),
            )
        }

        PinLockEvent.Ui.Backspace -> Next(
            state = state.copy(enteredPin = state.enteredPin.dropLast(1), isError = false),
        )

        PinLockEvent.Ui.BackClick -> Next(
            state = state,
            effect = PinLockEffect.NavigateBack,
        )

        PinLockEvent.PinVerified -> Next(
            state = state,
            effect = PinLockEffect.NavigateToNote(state.noteId),
        )

        PinLockEvent.PinRejected -> Next(
            state = state.copy(enteredPin = "", isError = true),
        )
    }
}
