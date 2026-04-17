package com.dreifus.app.features.notes.create.mvu

sealed interface CreateNoteEffect {
    data object Close : CreateNoteEffect
}
