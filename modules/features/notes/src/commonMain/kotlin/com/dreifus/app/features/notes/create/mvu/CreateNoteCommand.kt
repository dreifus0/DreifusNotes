package com.dreifus.app.features.notes.create.mvu

import com.dreifus.template.uikit.style.NoteCardColor

sealed interface CreateNoteCommand {
    data class CreateNote(
        val title: String,
        val description: String,
        val color: NoteCardColor,
    ) : CreateNoteCommand
}
