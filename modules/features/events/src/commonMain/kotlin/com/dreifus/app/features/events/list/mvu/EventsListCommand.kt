package com.dreifus.app.features.events.list.mvu

sealed interface EventsListCommand {
    data object ObserveUpcoming : EventsListCommand
}
