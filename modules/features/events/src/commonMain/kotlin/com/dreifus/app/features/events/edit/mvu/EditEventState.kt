package com.dreifus.app.features.events.edit.mvu

import androidx.compose.runtime.Immutable

@Immutable
data class EditEventState(
    val eventId: Long? = null,
    val title: String = "",
    /** Event moment as epoch milliseconds; seeded with the next full hour for a new event. */
    val at: Long = 0L,
    val isSaving: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
) {
    val isNew: Boolean get() = eventId == null
}
