package com.dreifus.app.features.settings.appearance.mvu

import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val AppearanceUpdate =
    Update<AppearanceState, AppearanceEvent, AppearanceCommand, AppearanceEffect> { state, event ->
        when (event) {
            AppearanceEvent.Ui.Init -> Next(
                state = state,
                command = AppearanceCommand.Observe,
            )
            is AppearanceEvent.Ui.Select -> Next(
                state = state.copy(selected = event.mode),
                command = AppearanceCommand.SetThemeMode(event.mode),
            )
            AppearanceEvent.Ui.BackClick -> Next(
                state = state,
                effect = AppearanceEffect.NavigateBack,
            )
            is AppearanceEvent.ThemeModeLoaded -> Next(
                state = state.copy(selected = event.mode),
            )
        }
    }
