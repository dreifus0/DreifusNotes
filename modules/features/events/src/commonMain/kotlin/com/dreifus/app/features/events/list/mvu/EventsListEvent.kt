package com.dreifus.app.features.events.list.mvu

sealed interface EventsListEvent {
    sealed interface Ui : EventsListEvent {
        data object Init : Ui
        data object BackClick : Ui
        data object AddClick : Ui
        data object ArchiveClick : Ui
        data class EventClick(val id: Long) : Ui
    }
    data class EventsLoaded(val sections: List<EventSectionUi>) : EventsListEvent
}
