package com.dreifus.app.features.notes.navigation

interface PinNavigation {
    fun openPinSetup(noteId: Long)
    fun openPinLock(noteId: Long, onUnlocked: (noteId: Long, pin: String) -> Unit)
}
