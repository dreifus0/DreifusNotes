package com.dreifus.app.features.pin.setup.mvu

sealed interface PinSetupEffect {
    data object NavigateBack : PinSetupEffect
    data class PinConfirmed(val noteId: Long, val pin: String) : PinSetupEffect
}
