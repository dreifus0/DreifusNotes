package com.dreifus.app.features.notes.create.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val CreateNoteUpdate = Update<CreateNoteState, CreateNoteEvent, CreateNoteCommand, CreateNoteEffect> { state, event ->
    when (event) {
        is CreateNoteEvent.Ui.TitleChanged -> Next(state = state.copy(title = event.title))
        is CreateNoteEvent.Ui.DescriptionChanged -> Next(state = state.copy(description = event.description))
        is CreateNoteEvent.Ui.ColorSelected -> Next(state = state.copy(selectedColor = event.color))
        is CreateNoteEvent.Ui.CreateClick -> if (state.title.isBlank()) Next(state) else Next(
            state = state.copy(isCreating = true),
            command = CreateNoteCommand.CreateNote(state.title, state.description, state.selectedColor),
        )
        CreateNoteEvent.NoteCreated -> Next(state = state.copy(isCreating = false), effect = CreateNoteEffect.Close)
    }
}
