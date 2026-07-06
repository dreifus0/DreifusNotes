package com.dreifus.app.features.notes.create.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.template.uikit.style.NoteCardColor

@Immutable
data class CreateNoteState(
    val title: String = "",
    val description: String = "",
    val availableColors: List<NoteCardColor> = NoteCardColor.DefaultFavorites,
    val selectedColor: NoteCardColor = NoteCardColor.Default,
    val isCreating: Boolean = false,
)
