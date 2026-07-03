package com.dreifus.app.features.settings.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.preferences.ThemePreferences
import com.dreifus.app.features.settings.appearance.mvu.AppearanceCommand
import com.dreifus.app.features.settings.appearance.mvu.AppearanceEffect
import com.dreifus.app.features.settings.appearance.mvu.AppearanceEvent
import com.dreifus.app.features.settings.appearance.mvu.AppearanceState
import com.dreifus.app.features.settings.appearance.mvu.AppearanceUpdate
import com.dreifus.app.features.settings.appearance.mvu.commandHandler.AppearanceObserveHandler
import com.dreifus.app.features.settings.appearance.mvu.commandHandler.AppearanceSetThemeModeHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(AppearanceViewModel::class)
@ContributesIntoMap(AppScope::class)
class AppearanceViewModel(
    themePreferences: ThemePreferences,
) : ViewModel() {

    private val store =
        Store<AppearanceState, AppearanceEvent, AppearanceEvent.Ui, AppearanceCommand, AppearanceEffect>(
            initialState = AppearanceState(),
            update = AppearanceUpdate,
            commandHandlers = listOf(
                AppearanceObserveHandler(themePreferences),
                AppearanceSetThemeModeHandler(themePreferences),
            ),
        )

    val state: StateFlow<AppearanceState> = store.state
    val effects: Flow<AppearanceEffect> = store.effects

    init {
        store.launch(viewModelScope)
    }

    fun dispatch(event: AppearanceEvent.Ui) = store.dispatch(event)
}
