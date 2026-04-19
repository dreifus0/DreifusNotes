package com.dreifus.app.features.notes.pin.setup.mvu

enum class PinSetupStep { ENTER, CONFIRM }

data class PinSetupState(
    val noteId: Long = 0L,
    val step: PinSetupStep = PinSetupStep.ENTER,
    val enteredPin: String = "",
    val firstPin: String = "",
    val isError: Boolean = false,
)
