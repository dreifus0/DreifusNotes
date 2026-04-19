package com.dreifus.app.features.pin.setup.mvu

sealed interface PinSetupEvent {
    sealed interface Ui : PinSetupEvent {
        data class Init(val noteId: Long) : Ui
        data class KeyPress(val digit: String) : Ui
        data object Backspace : Ui
        data object BackClick : Ui
    }
    data object PinSaved : PinSetupEvent
}
