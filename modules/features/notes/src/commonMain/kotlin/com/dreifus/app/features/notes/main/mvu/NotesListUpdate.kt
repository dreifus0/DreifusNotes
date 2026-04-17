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
            effect = NotesListEffect.NavigateToNote(event.id),
        )
    }
}
