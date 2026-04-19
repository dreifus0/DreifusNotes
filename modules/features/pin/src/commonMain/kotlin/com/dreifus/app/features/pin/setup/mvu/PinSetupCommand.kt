package com.dreifus.app.features.pin.setup.mvu

sealed interface PinSetupCommand {
    data class SavePin(val noteId: Long, val pin: String) : PinSetupCommand
}
