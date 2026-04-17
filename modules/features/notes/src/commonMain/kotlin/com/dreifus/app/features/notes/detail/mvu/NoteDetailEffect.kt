package com.dreifus.app.features.notes.detail.mvu

sealed interface NoteDetailEffect {
    data object NavigateBack : NoteDetailEffect
}
