package com.dreifus.app.features.settings.colors.mvu

import androidx.compose.runtime.Immutable
import com.dreifus.template.uikit.style.NoteCardColor

const val MAX_FAVORITE_COLORS = 10

@Immutable
data class NoteColorsState(
    val favorites: List<NoteCardColor> = NoteCardColor.DefaultFavorites,
    val highlighted: NoteCardColor? = null,
) {
    val canAdd: Boolean get() = favorites.size < MAX_FAVORITE_COLORS
    val canDelete: Boolean get() = highlighted != null && favorites.size > 1
}
