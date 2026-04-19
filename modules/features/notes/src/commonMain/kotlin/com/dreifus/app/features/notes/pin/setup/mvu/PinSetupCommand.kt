package com.dreifus.app.features.notes.pin.setup.mvu

sealed interface PinSetupCommand {
    data class SavePin(val noteId: Long, val pin: String) : PinSetupCommand
}
