package com.dreifus.template.uikit.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class NoteCardPalette(
    val background: Color,
    val title: Color,
    val body: Color,
    val date: Color,
)

enum class NoteCardColor {
    Purple, Pink, Green, Orange;

    @Composable
    fun palette(): NoteCardPalette {
        val isDark = AppTheme.colors.isDarkTheme
        return when (this) {
            Purple -> if (isDark) DarkPurple else LightPurple
            Pink -> if (isDark) DarkPink else LightPink
            Green -> if (isDark) DarkGreen else LightGreen
            Orange -> if (isDark) DarkOrange else LightOrange
        }
    }

    companion object {
        val Default = Purple
    }
}

private val LightPurple = NoteCardPalette(
    background = Color(0xFFCECBF6),
    title = Color(0xFF26215C),
    body = Color(0xFF3C3489),
    date = Color(0xFF534AB7),
)
private val LightPink = NoteCardPalette(
    background = Color(0xFFF4C0D1),
    title = Color(0xFF4B1528),
    body = Color(0xFF993556),
    date = Color(0xFF993556),
)
private val LightGreen = NoteCardPalette(
    background = Color(0xFF9FE1CB),
    title = Color(0xFF04342C),
    body = Color(0xFF085041),
    date = Color(0xFF0F6E56),
)
private val LightOrange = NoteCardPalette(
    background = Color(0xFFFAC775),
    title = Color(0xFF412402),
    body = Color(0xFF633806),
    date = Color(0xFF854F0B),
)

private val DarkPurple = NoteCardPalette(
    background = Color(0xFF3C3489),
    title = Color(0xFFEEEDFE),
    body = Color(0xFFAFA9EC),
    date = Color(0xFFAFA9EC),
)
private val DarkPink = NoteCardPalette(
    background = Color(0xFF72243E),
    title = Color(0xFFFBEAF0),
    body = Color(0xFFED93B1),
    date = Color(0xFFED93B1),
)
private val DarkGreen = NoteCardPalette(
    background = Color(0xFF085041),
    title = Color(0xFFE1F5EE),
    body = Color(0xFF5DCAA5),
    date = Color(0xFF5DCAA5),
)
private val DarkOrange = NoteCardPalette(
    background = Color(0xFF633806),
    title = Color(0xFFFAEEDA),
    body = Color(0xFFEF9F27),
    date = Color(0xFFEF9F27),
)
