package com.dreifus.app.features.notes.main.mvu

sealed interface NotesListEvent {
    sealed interface Ui : NotesListEvent {
        data object Init : Ui
        data class QueryChanged(val query: String) : Ui
        data object SearchToggleClick : Ui
        data object AddClick : Ui
        data class NoteClick(val id: Long) : Ui
        data object UpcomingEventClick : Ui
        data class NoteMoved(val fromIndex: Int, val toIndex: Int) : Ui
        data object NoteDragEnded : Ui
    }
    data class NotesLoaded(val notes: List<NoteUiItem>) : NotesListEvent
    data class UpcomingEventLoaded(val event: UpcomingEventUi?) : NotesListEvent
}
