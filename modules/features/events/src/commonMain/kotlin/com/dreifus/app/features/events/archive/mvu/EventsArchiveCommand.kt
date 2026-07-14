package com.dreifus.app.features.events.archive.mvu

sealed interface EventsArchiveCommand {
    data object ObservePast : EventsArchiveCommand
    data class DeleteEvent(val id: Long) : EventsArchiveCommand
}
