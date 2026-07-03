package com.dreifus.app.features.settings.main.mvu.commandHandler

import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.data.preferences.ThemePreferences
import com.dreifus.app.features.settings.main.mvu.SettingsCommand
import com.dreifus.app.features.settings.main.mvu.SettingsEvent
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SettingsCommandHandler(
    private val repository: NotesRepository,
) : FilteringHandlerToFlow<SettingsCommand.ResetAllData, SettingsCommand, SettingsEvent>(
    SettingsCommand.ResetAllData::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: SettingsCommand.ResetAllData): Flow<SettingsEvent> = flow {
        repository.deleteAll()
        emit(SettingsEvent.ResetComplete)
    }
}

class SettingsObserveThemeHandler(
    private val themePreferences: ThemePreferences,
) : FilteringHandlerToFlow<SettingsCommand.ObserveTheme, SettingsCommand, SettingsEvent>(
    SettingsCommand.ObserveTheme::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: SettingsCommand.ObserveTheme): Flow<SettingsEvent> =
        themePreferences.themeMode.map { SettingsEvent.ThemeModeLoaded(it) }
}
