package com.dreifus.app.features.settings.colors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.preferences.FavoriteColorsPreferences
import com.dreifus.app.features.settings.colors.mvu.NoteColorsCommand
import com.dreifus.app.features.settings.colors.mvu.NoteColorsEffect
import com.dreifus.app.features.settings.colors.mvu.NoteColorsEvent
import com.dreifus.app.features.settings.colors.mvu.NoteColorsState
import com.dreifus.app.features.settings.colors.mvu.NoteColorsUpdate
import com.dreifus.app.features.settings.colors.mvu.commandHandler.NoteColorsSetFavoritesHandler
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(NoteColorsViewModel::class)
@ContributesIntoMap(AppScope::class)
class NoteColorsViewModel(
    favoriteColorsPreferences: FavoriteColorsPreferences,
) : ViewModel() {

    private val store =
        Store<NoteColorsState, NoteColorsEvent, NoteColorsEvent.Ui, NoteColorsCommand, NoteColorsEffect>(
            // Favorites are available synchronously — seeding the initial state avoids
            // flashing the defaults before an async load lands.
            initialState = NoteColorsState(
                favorites = NoteCardColor.favoritesFrom(favoriteColorsPreferences.favorites.value),
            ),
            update = NoteColorsUpdate,
            commandHandlers = listOf(
                NoteColorsSetFavoritesHandler(favoriteColorsPreferences),
            ),
        )

    val state: StateFlow<NoteColorsState> = store.state
    val effects: Flow<NoteColorsEffect> = store.effects

    init {
        store.launch(viewModelScope)
    }

    fun dispatch(event: NoteColorsEvent.Ui) = store.dispatch(event)
}
