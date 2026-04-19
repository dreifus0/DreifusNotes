package com.dreifus.app.features.pin.lock.mvu

sealed interface PinLockCommand {
    data class VerifyPin(val noteId: Long, val pin: String) : PinLockCommand
}
