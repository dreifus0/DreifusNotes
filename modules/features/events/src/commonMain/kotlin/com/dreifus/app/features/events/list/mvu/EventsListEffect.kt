package com.dreifus.app.features.events.list.mvu

sealed interface EventsListEffect {
    data object NavigateBack : EventsListEffect
    data object NavigateToArchive : EventsListEffect
    /** [id] is null for a new event. */
    data class NavigateToEdit(val id: Long?) : EventsListEffect
}
