package com.dreifus.app.features.events.archive.mvu

sealed interface EventsArchiveEvent {
    sealed interface Ui : EventsArchiveEvent {
        data object Init : Ui
        data object BackClick : Ui
        data class DeleteClick(val id: Long) : Ui
    }
    data class ArchiveLoaded(val items: List<ArchivedEventUi>) : EventsArchiveEvent
}
