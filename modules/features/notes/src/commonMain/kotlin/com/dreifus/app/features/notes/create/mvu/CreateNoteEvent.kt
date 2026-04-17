package com.dreifus.app.features.notes.create.mvu

import com.dreifus.template.uikit.style.NoteCardColor

sealed interface CreateNoteEvent {
    sealed interface Ui : CreateNoteEvent {
        data class TitleChanged(val title: String) : Ui
        data class DescriptionChanged(val description: String) : Ui
        data class ColorSelected(val color: NoteCardColor) : Ui
        data object CreateClick : Ui
    }

    data object NoteCreated : CreateNoteEvent
}
