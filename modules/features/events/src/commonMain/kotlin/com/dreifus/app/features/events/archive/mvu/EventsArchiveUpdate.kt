package com.dreifus.app.features.events.archive.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val EventsArchiveUpdate =
    Update<EventsArchiveState, EventsArchiveEvent, EventsArchiveCommand, EventsArchiveEffect> { state, event ->
        when (event) {
            is EventsArchiveEvent.Ui.Init -> Next(
                state = state,
                command = EventsArchiveCommand.ObservePast,
            )
            is EventsArchiveEvent.ArchiveLoaded -> Next(
                state = state.copy(items = event.items, isLoading = false),
            )
            is EventsArchiveEvent.Ui.BackClick -> Next(
                state = state,
                effect = EventsArchiveEffect.NavigateBack,
            )
            is EventsArchiveEvent.Ui.DeleteClick -> Next(
                state = state,
                command = EventsArchiveCommand.DeleteEvent(event.id),
            )
        }
    }
