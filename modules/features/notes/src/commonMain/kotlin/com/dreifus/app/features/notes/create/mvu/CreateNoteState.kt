package com.dreifus.app.features.notes.create.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.template.uikit.style.NoteCardColor

@Immutable
data class CreateNoteState(
    val title: String = "",
    val description: String = "",
    val selectedColor: NoteCardColor = NoteCardColor.Purple,
    val isCreating: Boolean = false,
)
