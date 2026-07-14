package com.dreifus.app.features.events.edit.mvu

sealed interface EditEventCommand {
    data class LoadEvent(val id: Long) : EditEventCommand
    /** [id] is null when creating a new event. */
    data class SaveEvent(val id: Long?, val title: String, val at: Long) : EditEventCommand
    data class DeleteEvent(val id: Long) : EditEventCommand
}
