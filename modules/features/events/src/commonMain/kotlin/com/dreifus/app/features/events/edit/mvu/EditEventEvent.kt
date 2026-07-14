package com.dreifus.app.features.events.edit.mvu

import com.dreifus.app.data.notes.model.Event

sealed interface EditEventEvent {
    sealed interface Ui : EditEventEvent {
        data class Init(val eventId: Long?) : Ui
        data class TitleChanged(val title: String) : Ui
        data object DateClick : Ui
        data object TimeClick : Ui
        data object PickerDismissed : Ui
        /** UTC-midnight millis of the picked day, as reported by the material date picker. */
        data class DatePicked(val utcDateMillis: Long) : Ui
        data class TimePicked(val hour: Int, val minute: Int) : Ui
        data object SaveClick : Ui
        data object DeleteClick : Ui
    }
    data class EventLoaded(val event: Event) : EditEventEvent
    data object Saved : EditEventEvent
    data object Deleted : EditEventEvent
}
