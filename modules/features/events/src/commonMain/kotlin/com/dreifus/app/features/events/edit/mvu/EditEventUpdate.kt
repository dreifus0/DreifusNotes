package com.dreifus.app.features.events.edit.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

val EditEventUpdate = Update<EditEventState, EditEventEvent, EditEventCommand, EditEventEffect> { state, event ->
    when (event) {
        is EditEventEvent.Ui.Init -> if (event.eventId != null) Next(
            state = state.copy(eventId = event.eventId),
            command = EditEventCommand.LoadEvent(event.eventId),
        ) else Next(state)
        is EditEventEvent.EventLoaded -> Next(
            state = state.copy(title = event.event.title, at = event.event.at),
        )
        is EditEventEvent.Ui.TitleChanged -> Next(state = state.copy(title = event.title))
        is EditEventEvent.Ui.DateClick -> Next(state = state.copy(showDatePicker = true))
        is EditEventEvent.Ui.TimeClick -> Next(state = state.copy(showTimePicker = true))
        is EditEventEvent.Ui.PickerDismissed -> Next(
            state = state.copy(showDatePicker = false, showTimePicker = false),
        )
        is EditEventEvent.Ui.DatePicked -> Next(
            state = state.copy(at = state.at.withDateFrom(event.utcDateMillis), showDatePicker = false),
        )
        is EditEventEvent.Ui.TimePicked -> Next(
            state = state.copy(at = state.at.withTime(event.hour, event.minute), showTimePicker = false),
        )
        is EditEventEvent.Ui.SaveClick -> if (state.title.isBlank()) Next(state) else Next(
            state = state.copy(isSaving = true),
            command = EditEventCommand.SaveEvent(state.eventId, state.title.trim(), state.at),
        )
        is EditEventEvent.Ui.DeleteClick -> state.eventId?.let { id ->
            Next(state = state, command = EditEventCommand.DeleteEvent(id))
        } ?: Next(state)
        is EditEventEvent.Saved -> Next(
            state = state.copy(isSaving = false),
            effect = EditEventEffect.Close,
        )
        is EditEventEvent.Deleted -> Next(state = state, effect = EditEventEffect.Close)
    }
}

/** Keeps the local time of day, replacing the day with the one picked in the date picker. */
private fun Long.withDateFrom(utcDateMillis: Long): Long {
    val zone = TimeZone.currentSystemDefault()
    val pickedDate = Instant.fromEpochMilliseconds(utcDateMillis).toLocalDateTime(TimeZone.UTC).date
    val time = Instant.fromEpochMilliseconds(this).toLocalDateTime(zone).time
    return LocalDateTime(pickedDate, time).toInstant(zone).toEpochMilliseconds()
}

/** Keeps the local day, replacing the time of day. */
private fun Long.withTime(hour: Int, minute: Int): Long {
    val zone = TimeZone.currentSystemDefault()
    val date = Instant.fromEpochMilliseconds(this).toLocalDateTime(zone).date
    return LocalDateTime(date.year, date.month, date.dayOfMonth, hour, minute)
        .toInstant(zone).toEpochMilliseconds()
}
