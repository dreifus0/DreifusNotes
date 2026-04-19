package com.dreifus.app.features.notes.pin.lock.mvu

data class PinLockState(
    val noteId: Long = 0L,
    val enteredPin: String = "",
    val isError: Boolean = false,
)
