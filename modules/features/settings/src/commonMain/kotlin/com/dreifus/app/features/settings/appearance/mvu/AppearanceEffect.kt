package com.dreifus.app.features.settings.appearance.mvu

sealed interface AppearanceEffect {
    data object NavigateBack : AppearanceEffect
}
