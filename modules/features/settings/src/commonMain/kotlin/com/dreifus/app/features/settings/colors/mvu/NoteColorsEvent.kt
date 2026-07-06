package com.dreifus.app.features.settings.colors.mvu

import com.dreifus.template.uikit.style.NoteCardColor

sealed interface NoteColorsEvent {
    sealed interface Ui : NoteColorsEvent {
        data class ColorTap(val color: NoteCardColor) : Ui
        data class AddColor(val color: NoteCardColor) : Ui
        data object DeleteClick : Ui
        data object BackClick : Ui
    }
}
