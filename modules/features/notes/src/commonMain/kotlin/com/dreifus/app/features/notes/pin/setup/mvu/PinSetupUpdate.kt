package com.dreifus.app.features.notes.pin.setup.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

private const val PIN_LENGTH = 4

val PinSetupUpdate = Update<PinSetupState, PinSetupEvent, PinSetupCommand, PinSetupEffect> { state, event ->
    when (event) {
        is PinSetupEvent.Ui.Init -> Next(state = state.copy(noteId = event.noteId))

        is PinSetupEvent.Ui.KeyPress -> {
            if (state.enteredPin.length >= PIN_LENGTH) return@Update Next(state)
            val newPin = state.enteredPin + event.digit
            if (newPin.length < PIN_LENGTH) return@Update Next(state.copy(enteredPin = newPin, isError = false))

            when (state.step) {
                PinSetupStep.ENTER -> Next(
                    state = state.copy(
                        step = PinSetupStep.CONFIRM,
                        firstPin = newPin,
                        enteredPin = "",
                        isError = false,
                    ),
                )
                PinSetupStep.CONFIRM -> if (newPin == state.firstPin) {
                    Next(
                        state = state.copy(enteredPin = newPin),
                        command = PinSetupCommand.SavePin(state.noteId, newPin),
                    )
                } else {
                    Next(
                        state = state.copy(
                            step = PinSetupStep.ENTER,
                            enteredPin = "",
                            firstPin = "",
                            isError = true,
                        ),
                    )
                }
            }
        }

        PinSetupEvent.Ui.Backspace -> Next(
            state = state.copy(enteredPin = state.enteredPin.dropLast(1), isError = false),
        )

        PinSetupEvent.Ui.BackClick -> when (state.step) {
            PinSetupStep.ENTER -> Next(state, effect = PinSetupEffect.NavigateBack)
            PinSetupStep.CONFIRM -> Next(
                state = state.copy(
                    step = PinSetupStep.ENTER,
                    enteredPin = "",
                    firstPin = "",
                    isError = false,
                ),
            )
        }

        PinSetupEvent.PinSaved -> Next(state, effect = PinSetupEffect.NavigateBack)
    }
}
