package com.dreifus.app.features.settings.colors.mvu

import com.dreifus.template.uikit.style.NoteCardColor

sealed interface NoteColorsCommand {
    data class SetFavorites(val colors: List<NoteCardColor>) : NoteColorsCommand
}
