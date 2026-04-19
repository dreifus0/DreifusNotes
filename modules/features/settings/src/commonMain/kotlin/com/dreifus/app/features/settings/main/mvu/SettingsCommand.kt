package com.dreifus.app.features.settings.main.mvu

sealed interface SettingsCommand {
    data object ResetAllData : SettingsCommand
}
