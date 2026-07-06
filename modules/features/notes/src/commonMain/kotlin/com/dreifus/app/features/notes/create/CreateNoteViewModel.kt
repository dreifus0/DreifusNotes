package com.dreifus.app.features.notes.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.data.notes.NotesRepository
import com.dreifus.app.data.preferences.FavoriteColorsPreferences
import com.dreifus.app.features.notes.create.mvu.CreateNoteCommand
import com.dreifus.app.features.notes.create.mvu.CreateNoteEffect
import com.dreifus.app.features.notes.create.mvu.CreateNoteEvent
import com.dreifus.app.features.notes.create.mvu.CreateNoteState
import com.dreifus.app.features.notes.create.mvu.CreateNoteUpdate
import com.dreifus.app.features.notes.create.mvu.commandHandler.CreateNoteCommandHandler
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey(CreateNoteViewModel::class)
@ContributesIntoMap(AppScope::class)
class CreateNoteViewModel(
    repository: NotesRepository,
    favoriteColorsPreferences: FavoriteColorsPreferences,
) : ViewModel() {

    // Favorites are available synchronously, so seed the initial state with them —
    // loading through a command would flash the default colors first.
    private val favoriteColors = NoteCardColor.favoritesFrom(favoriteColorsPreferences.favorites.value)

    private val store =
        Store<CreateNoteState, CreateNoteEvent, CreateNoteEvent.Ui, CreateNoteCommand, CreateNoteEffect>(
            initialState = CreateNoteState(
                availableColors = favoriteColors,
                selectedColor = favoriteColors.first(),
            ),
            update = CreateNoteUpdate,
            commandHandlers = listOf(CreateNoteCommandHandler(repository)),
        )

    val state: StateFlow<CreateNoteState> = store.state
    val effects: Flow<CreateNoteEffect> = store.effects

    init {
        store.launch(viewModelScope)
    }

    fun dispatch(event: CreateNoteEvent.Ui) = store.dispatch(event)
}
