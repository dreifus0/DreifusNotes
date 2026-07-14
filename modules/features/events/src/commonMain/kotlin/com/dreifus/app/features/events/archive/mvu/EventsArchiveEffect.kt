package com.dreifus.app.features.events.archive.mvu

sealed interface EventsArchiveEffect {
    data object NavigateBack : EventsArchiveEffect
}
