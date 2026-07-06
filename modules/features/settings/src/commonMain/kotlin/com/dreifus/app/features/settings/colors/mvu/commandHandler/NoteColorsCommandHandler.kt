package com.dreifus.app.features.settings.colors.mvu.commandHandler

import com.dreifus.app.data.preferences.FavoriteColorsPreferences
import com.dreifus.app.features.settings.colors.mvu.NoteColorsCommand
import com.dreifus.app.features.settings.colors.mvu.NoteColorsEvent
import com.dreifus.template.uikit.style.NoteCardColor
import com.yavorcool.mvucore.FilteringHandlerToFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteColorsSetFavoritesHandler(
    private val favoriteColorsPreferences: FavoriteColorsPreferences,
) : FilteringHandlerToFlow<NoteColorsCommand.SetFavorites, NoteColorsCommand, NoteColorsEvent>(
    NoteColorsCommand.SetFavorites::class,
    cancelPreviousOnNewCommand = false,
) {
    override suspend fun handleCommand(command: NoteColorsCommand.SetFavorites): Flow<NoteColorsEvent> =
        flow { favoriteColorsPreferences.setFavorites(command.colors.map(NoteCardColor::serialize)) }
}
