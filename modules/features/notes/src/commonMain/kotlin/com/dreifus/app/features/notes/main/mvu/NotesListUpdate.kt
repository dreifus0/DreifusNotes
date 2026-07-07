package com.dreifus.app.features.notes.main.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val NotesListUpdate = Update<NotesListState, NotesListEvent, NotesListCommand, NotesListEffect> { state, event ->
    when (event) {
        is NotesListEvent.Ui.QueryChanged -> Next(
            state = state.copy(query = event.query, isLoading = true),
            command = NotesListCommand.ObserveNotes(event.query),
        )
        is NotesListEvent.NotesLoaded -> Next(
            state = state.copy(notes = event.notes, isLoading = false),
        )
        is NotesListEvent.Ui.AddClick -> Next(
            state = state,
            effect = NotesListEffect.NavigateToNewNote,
        )
        is NotesListEvent.Ui.NoteClick -> Next(
            state = state,
            effect = NotesListEffect.NavigateToNote(
                id = event.id,
                isProtected = state.notes.find { it.id == event.id }?.isProtected ?: false,
            ),
        )
        is NotesListEvent.Ui.NoteMoved -> {
            val notes = state.notes.toMutableList()
            if (event.fromIndex in notes.indices && event.toIndex in notes.indices) {
                notes.add(event.toIndex, notes.removeAt(event.fromIndex))
            }
            Next(state = state.copy(notes = notes))
        }
        is NotesListEvent.Ui.NoteDragEnded -> Next(
            state = state,
            command = NotesListCommand.PersistOrder(state.notes.map { it.id }),
        )
    }
}
