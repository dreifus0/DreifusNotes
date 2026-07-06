package com.dreifus.app.features.settings.colors.mvu

sealed interface NoteColorsEffect {
    data object NavigateBack : NoteColorsEffect
}
