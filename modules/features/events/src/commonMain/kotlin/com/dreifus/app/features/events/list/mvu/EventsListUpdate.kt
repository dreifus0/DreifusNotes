package com.dreifus.app.features.events.list.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val EventsListUpdate = Update<EventsListState, EventsListEvent, EventsListCommand, EventsListEffect> { state, event ->
    when (event) {
        is EventsListEvent.Ui.Init -> Next(
            state = state,
            command = EventsListCommand.ObserveUpcoming,
        )
        is EventsListEvent.EventsLoaded -> Next(
            state = state.copy(sections = event.sections, isLoading = false),
        )
        is EventsListEvent.Ui.BackClick -> Next(
            state = state,
            effect = EventsListEffect.NavigateBack,
        )
        is EventsListEvent.Ui.AddClick -> Next(
            state = state,
            effect = EventsListEffect.NavigateToEdit(null),
        )
        is EventsListEvent.Ui.ArchiveClick -> Next(
            state = state,
            effect = EventsListEffect.NavigateToArchive,
        )
        is EventsListEvent.Ui.EventClick -> Next(
            state = state,
            effect = EventsListEffect.NavigateToEdit(event.id),
        )
    }
}
