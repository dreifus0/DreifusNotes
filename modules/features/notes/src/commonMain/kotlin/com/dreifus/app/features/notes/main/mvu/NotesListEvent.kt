package com.dreifus.app.features.notes.main.mvu

sealed interface NotesListEvent {
    sealed interface Ui : NotesListEvent {
        data class QueryChanged(val query: String) : Ui
        data object AddClick : Ui
        data class NoteClick(val id: Long) : Ui
    }
    data class NotesLoaded(val notes: List<NoteUiItem>) : NotesListEvent
}
