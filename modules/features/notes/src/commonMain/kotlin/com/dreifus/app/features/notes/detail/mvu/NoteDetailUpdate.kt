package com.dreifus.app.features.notes.detail.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val NoteDetailUpdate = Update<NoteDetailState, NoteDetailEvent, NoteDetailCommand, NoteDetailEffect> { state, event ->
    when (event) {
        is NoteDetailEvent.Ui.Init -> Next(
            state = state.copy(noteId = event.noteId, isLoading = true, unlockedPin = event.unlockedPin),
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
            state = state.copy(blocks = event.blocks),
        )
        NoteDetailEvent.BlockSent -> Next(state = state)
        NoteDetailEvent.BlockDeleted -> Next(state = state)
    }
}
