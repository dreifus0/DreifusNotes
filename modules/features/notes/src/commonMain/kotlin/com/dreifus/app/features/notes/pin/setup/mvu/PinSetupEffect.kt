package com.dreifus.app.features.notes.pin.setup.mvu

sealed interface PinSetupEffect {
    data object NavigateBack : PinSetupEffect
}
