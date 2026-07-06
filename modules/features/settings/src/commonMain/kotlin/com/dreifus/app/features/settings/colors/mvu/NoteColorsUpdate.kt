package com.dreifus.app.features.settings.colors.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val NoteColorsUpdate =
    Update<NoteColorsState, NoteColorsEvent, NoteColorsCommand, NoteColorsEffect> { state, event ->
        when (event) {
            is NoteColorsEvent.Ui.ColorTap -> Next(
                state = state.copy(highlighted = if (state.highlighted == event.color) null else event.color),
            )
            is NoteColorsEvent.Ui.AddColor -> {
                if (event.color in state.favorites || !state.canAdd) Next(state) else {
                    val next = state.favorites + event.color
                    Next(
                        state = state.copy(favorites = next),
                        command = NoteColorsCommand.SetFavorites(next),
                    )
                }
            }
            NoteColorsEvent.Ui.DeleteClick -> {
                // Keep at least one favorite so pickers always have something to offer.
                if (!state.canDelete) Next(state) else {
                    val next = state.favorites - state.highlighted!!
                    Next(
                        state = state.copy(favorites = next, highlighted = null),
                        command = NoteColorsCommand.SetFavorites(next),
                    )
                }
            }
            NoteColorsEvent.Ui.BackClick -> Next(
                state = state,
                effect = NoteColorsEffect.NavigateBack,
            )
        }
    }
