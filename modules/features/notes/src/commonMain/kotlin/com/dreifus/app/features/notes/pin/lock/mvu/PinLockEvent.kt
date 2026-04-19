package com.dreifus.app.features.notes.pin.lock.mvu

sealed interface PinLockEvent {
    sealed interface Ui : PinLockEvent {
        data class Init(val noteId: Long) : Ui
        data class KeyPress(val digit: String) : Ui
        data object Backspace : Ui
        data object BackClick : Ui
    }

    data object PinVerified : PinLockEvent
    data object PinRejected : PinLockEvent
}
