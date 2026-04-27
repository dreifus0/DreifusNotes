package com.dreifus.app.features.notes.detail.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val NoteDetailUpdate = Update<NoteDetailState, NoteDetailEvent, NoteDetailCommand, NoteDetailEffect> { state, event ->
    when (event) {
        is NoteDetailEvent.Ui.Init -> Next(
            state = state.copy(noteId = event.noteId, isLoading = true, isBlocksLoading = true, unlockedPin = event.unlockedPin),
            command = NoteDetailCommand.Init(event.noteId, event.unlockedPin),
        )
        NoteDetailEvent.Ui.BackClick -> Next(
            state = state,
            effect = NoteDetailEffect.NavigateBack,
        )
        is NoteDetailEvent.Ui.InputChanged -> Next(
            state = state.copy(inputText = event.text),
        )
        NoteDetailEvent.Ui.SendClick -> if (state.inputText.isBlank()) Next(state) else Next(
            state = state.copy(inputText = ""),
            command = NoteDetailCommand.InsertBlock(state.noteId, state.inputText, state.unlockedPin),
        )
        is NoteDetailEvent.Ui.DeleteBlockClick -> Next(
            state = state,
            command = NoteDetailCommand.DeleteBlock(event.blockId),
        )
        is NoteDetailEvent.Ui.CopyBlockClick -> {
            val text = state.blocks.find { it.id == event.blockId }?.copyText()
            if (text != null) Next(state = state, effect = NoteDetailEffect.CopyToClipboard(text))
            else Next(state = state)
        }
        is NoteDetailEvent.Ui.EditBlockConfirmed -> Next(
            state = state,
            command = NoteDetailCommand.UpdateBlock(event.blockId, event.newText, state.unlockedPin),
        )
        NoteDetailEvent.Ui.LockClick -> Next(
            state = state,
            effect = NoteDetailEffect.NavigateToPinSetup(state.noteId),
        )
        NoteDetailEvent.Ui.PhotoClick -> Next(
            state = state,
            effect = NoteDetailEffect.ShowImagePicker,
        )
        NoteDetailEvent.Ui.ChecklistClick -> Next(
            state = state,
            effect = NoteDetailEffect.ShowChecklistSheet,
        )
        is NoteDetailEvent.Ui.PhotoSelected -> if (event.uri.isBlank()) Next(state) else Next(
            state = state,
            command = NoteDetailCommand.InsertPhotoBlock(state.noteId, event.uri, state.unlockedPin),
        )
        is NoteDetailEvent.Ui.ChecklistConfirmed -> if (event.items.isEmpty()) Next(state) else Next(
            state = state,
            command = NoteDetailCommand.InsertChecklistBlock(state.noteId, event.title, event.items, state.unlockedPin),
        )
        is NoteDetailEvent.NoteLoaded -> Next(
            state = state.copy(title = event.title, color = event.color, isLoading = false),
        )
        is NoteDetailEvent.BlocksLoaded -> Next(
            state = state.copy(blocks = event.blocks, isBlocksLoading = false),
        )
        NoteDetailEvent.BlockSent -> Next(state = state)
        NoteDetailEvent.BlockDeleted -> Next(state = state)
        NoteDetailEvent.BlockUpdated -> Next(state = state)
        NoteDetailEvent.Ui.ShareClick -> Next(
            state = state,
            effect = NoteDetailEffect.ShareNote(buildShareText(state)),
        )
        is NoteDetailEvent.Ui.RenameConfirmed -> Next(
            state = state.copy(title = event.newTitle),
            command = NoteDetailCommand.RenameNote(state.noteId, event.newTitle),
        )
        is NoteDetailEvent.Ui.ColorChangeConfirmed -> Next(
            state = state.copy(color = event.color),
            command = NoteDetailCommand.ChangeNoteColor(state.noteId, event.color),
        )
        NoteDetailEvent.Ui.DeleteNoteConfirmed -> Next(
            state = state,
            command = NoteDetailCommand.DeleteNote(state.noteId),
        )
        NoteDetailEvent.NoteRenamed -> Next(state = state)
        NoteDetailEvent.NoteColorChanged -> Next(state = state)
        NoteDetailEvent.NoteDeleted -> Next(state = state, effect = NoteDetailEffect.NavigateBack)
    }
}

private fun buildShareText(state: NoteDetailState): String = buildString {
    if (state.title.isNotBlank()) {
        appendLine(state.title)
        appendLine()
    }
    state.blocks.forEach { block ->
        when (block) {
            is NoteBlockUiItem.Text -> appendLine(block.text)
            is NoteBlockUiItem.Checklist -> {
                if (block.title.isNotBlank()) appendLine(block.title)
                block.items.forEach { appendLine("• $it") }
            }
            is NoteBlockUiItem.Photo -> Unit
        }
        appendLine()
    }
}.trimEnd()

private fun NoteBlockUiItem.copyText(): String? = when (this) {
    is NoteBlockUiItem.Text -> text
    is NoteBlockUiItem.Checklist -> buildString {
        if (title.isNotBlank()) appendLine(title)
        items.forEach(::appendLine)
    }.trimEnd()
    is NoteBlockUiItem.Photo -> null
}
