package com.dreifus.app.features.events.edit.mvu

sealed interface EditEventEffect {
    data object Close : EditEventEffect
}
