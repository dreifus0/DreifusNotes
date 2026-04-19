package com.dreifus.app.features.pin.lock.mvu

sealed interface PinLockEffect {
    data class NavigateToNote(val noteId: Long) : PinLockEffect
    data object NavigateBack : PinLockEffect
}
