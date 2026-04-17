package com.dreifus.app.features.notes.detail.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val NoteDetailUpdate = Update<NoteDetailState, NoteDetailEvent, NoteDetailCommand, NoteDetailEffect> { state, event ->
    when (event) {
        is NoteDetailEvent.Ui.Init -> Next(
            state = state.copy(noteId = event.noteId, isLoading = true),
            command = NoteDetailCommand.Init(event.noteId),
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
            command = NoteDetailCommand.InsertBlock(state.noteId, state.inputText),
        )
        is NoteDetailEvent.Ui.DeleteBlockClick -> Next(
            state = state,
            command = NoteDetailCommand.DeleteBlock(event.blockId),
        )
        is NoteDetailEvent.NoteLoaded -> Next(
            state = state.copy(title = event.title, color = event.color, isLoading = false),
        )
        is NoteDetailEvent.BlocksLoaded -> Next(
            state = state.copy(blocks = event.blocks),
        )
        NoteDetailEvent.BlockSent -> Next(state = state)
        NoteDetailEvent.BlockDeleted -> Next(state = state)
    }
}
