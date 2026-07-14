package com.dreifus.app.features.notes.main.mvu

sealed interface NotesListCommand {
    data class ObserveNotes(val query: String) : NotesListCommand
    data object ObserveUpcomingEvent : NotesListCommand
    data class PersistOrder(val orderedIds: List<Long>) : NotesListCommand
}
