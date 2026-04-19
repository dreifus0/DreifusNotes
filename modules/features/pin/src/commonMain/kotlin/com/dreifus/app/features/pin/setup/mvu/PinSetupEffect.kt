package com.dreifus.app.features.pin.setup.mvu

sealed interface PinSetupEffect {
    data object NavigateBack : PinSetupEffect
}
