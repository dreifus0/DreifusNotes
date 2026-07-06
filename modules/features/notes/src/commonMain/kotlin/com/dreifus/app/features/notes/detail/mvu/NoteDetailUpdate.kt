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
            val block = state.blocks.find { it.id == event.blockId }
            when {
                block is NoteBlockUiItem.Photo -> Next(state = state, effect = NoteDetailEffect.CopyImage(block.uri))
                block != null -> Next(state = state, effect = NoteDetailEffect.CopyToClipboard(block.copyText().orEmpty()))
                else -> Next(state = state)
            }
        }
        is NoteDetailEvent.Ui.ShareBlockClick -> {
            val block = state.blocks.find { it.id == event.blockId }
            when {
                block is NoteBlockUiItem.Photo -> Next(state = state, effect = NoteDetailEffect.ShareImage(block.uri))
                block != null -> Next(state = state, effect = NoteDetailEffect.ShareNote(block.copyText().orEmpty()))
                else -> Next(state = state)
            }
        }
        is NoteDetailEvent.Ui.EditBlockConfirmed -> Next(
            state = state,
            command = NoteDetailCommand.UpdateBlock(event.blockId, event.newText, state.unlockedPin),
        )
        NoteDetailEvent.Ui.LockClick -> Next(
            state = state,
            effect = NoteDetailEffect.NavigateToPinSetup(state.noteId),
        )
        NoteDetailEvent.Ui.UnlockConfirmed -> {
            val pin = state.unlockedPin
            if (pin == null) Next(state) else Next(
                state = state,
                command = NoteDetailCommand.RemoveProtection(state.noteId, pin),
            )
        }
        is NoteDetailEvent.Ui.DescriptionConfirmed -> Next(
            state = state.copy(description = event.description),
            command = NoteDetailCommand.UpdateDescription(state.noteId, event.description, state.unlockedPin),
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
        is NoteDetailEvent.Ui.ChecklistItemToggled -> {
            val block = state.blocks.find { it.id == event.blockId } as? NoteBlockUiItem.Checklist
            if (block == null || event.itemIndex !in block.items.indices) Next(state) else {
                val toggled = block.copy(
                    items = block.items.mapIndexed { index, item ->
                        if (index == event.itemIndex) item.copy(isChecked = !item.isChecked) else item
                    },
                )
                Next(
                    // Optimistic update for an instant checkbox; the blocks flow re-emits after save.
                    state = state.copy(blocks = state.blocks.map { if (it.id == block.id) toggled else it }),
                    command = NoteDetailCommand.UpdateBlock(block.id, toggled.encodeToBlockText(), state.unlockedPin),
                )
            }
        }
        is NoteDetailEvent.NoteLoaded -> Next(
            state = state.copy(
                title = event.title,
                description = event.description,
                color = event.color,
                isProtected = event.isProtected,
                updatedAt = event.updatedAt,
                isLoading = false,
            ),
        )
        is NoteDetailEvent.BlocksLoaded -> Next(
            state = state.copy(blocks = event.blocks, isBlocksLoading = false),
        )
        is NoteDetailEvent.FavoriteColorsLoaded -> Next(
            state = state.copy(favoriteColors = event.colors),
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
        NoteDetailEvent.NoteDescriptionChanged -> Next(state = state)
        NoteDetailEvent.NoteUnlocked -> Next(
            state = state.copy(isProtected = false, unlockedPin = null),
        )
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
                block.items.forEach { appendLine("${if (it.isChecked) "✓" else "•"} ${it.text}") }
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
        items.forEach { appendLine("${if (it.isChecked) "✓" else "•"} ${it.text}") }
    }.trimEnd()
    is NoteBlockUiItem.Photo -> null
}
