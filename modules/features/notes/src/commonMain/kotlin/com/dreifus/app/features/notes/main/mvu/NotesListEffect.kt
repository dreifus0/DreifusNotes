package com.dreifus.app.features.notes.main.mvu

sealed interface NotesListEffect {
    data object NavigateToNewNote : NotesListEffect
    data class NavigateToNote(val id: Long) : NotesListEffect
}
