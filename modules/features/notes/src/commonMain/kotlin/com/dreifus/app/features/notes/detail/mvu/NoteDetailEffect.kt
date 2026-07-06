package com.dreifus.app.features.notes.detail.mvu

sealed interface NoteDetailEffect {
    data object NavigateBack : NoteDetailEffect
    data class NavigateToPinSetup(val noteId: Long) : NoteDetailEffect
    data object ShowImagePicker : NoteDetailEffect
    data object ShowChecklistSheet : NoteDetailEffect
    data class CopyToClipboard(val text: String) : NoteDetailEffect
    data class ShareNote(val text: String) : NoteDetailEffect
    data class CopyImage(val uri: String) : NoteDetailEffect
    data class ShareImage(val uri: String) : NoteDetailEffect
}
