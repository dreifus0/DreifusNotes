package com.dreifus.app.features.settings.appearance.mvu.commandHandler

import com.dreifus.app.data.preferences.ThemePreferences
import com.dreifus.app.features.settings.appearance.mvu.AppearanceCommand
import com.dreifus.app.features.settings.appearance.mvu.AppearanceEvent
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AppearanceObserveHandler(
    private val themePreferences: ThemePreferences,
) : FilteringHandlerToFlow<AppearanceCommand.Observe, AppearanceCommand, AppearanceEvent>(
    AppearanceCommand.Observe::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: AppearanceCommand.Observe): Flow<AppearanceEvent> =
        themePreferences.themeMode.map { AppearanceEvent.ThemeModeLoaded(it) }
}

class AppearanceSetThemeModeHandler(
    private val themePreferences: ThemePreferences,
) : FilteringHandlerToFlow<AppearanceCommand.SetThemeMode, AppearanceCommand, AppearanceEvent>(
    AppearanceCommand.SetThemeMode::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: AppearanceCommand.SetThemeMode): Flow<AppearanceEvent> =
        flow { themePreferences.setThemeMode(command.mode) }
}
