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

/**
 * A note's accent color, seeded by a single [seed] color. The palette is theme-independent: the
 * card background is always the picked [seed], so cards match the color pickers in both themes.
 * The eight built-in presets keep their hand-tuned text colors; any other (user-picked) seed gets
 * text colors derived from its hue.
 */
@Immutable
data class NoteCardColor(val seed: Color) {

    @Composable
    fun palette(): NoteCardPalette = presetPalettes[seed] ?: derivedPalette(seed)

    /** Stored form: ARGB hex, e.g. `#FFCECBF6`. */
    fun serialize(): String = "#" + seed.toArgbLong().toString(16).padStart(8, '0').uppercase()

    companion object {
        val Purple = NoteCardColor(Color(0xFFCECBF6))
        val Pink = NoteCardColor(Color(0xFFF4C0D1))
        val Green = NoteCardColor(Color(0xFF9FE1CB))
        val Orange = NoteCardColor(Color(0xFFFAC775))
        val Blue = NoteCardColor(Color(0xFFBBD6F6))
        val Teal = NoteCardColor(Color(0xFFA8E3E0))
        val Red = NoteCardColor(Color(0xFFF6BFB7))
        val Yellow = NoteCardColor(Color(0xFFEDE28B))

        val Default = Purple

        /** The colors offered before the user picks their own favorites. */
        val DefaultFavorites = listOf(Purple, Pink, Green, Orange)

        // Notes persisted before arbitrary colors stored the palette by enum name.
        private val legacyNames = mapOf(
            "Purple" to Purple,
            "Pink" to Pink,
            "Green" to Green,
            "Orange" to Orange,
            "Blue" to Blue,
            "Teal" to Teal,
            "Red" to Red,
            "Yellow" to Yellow,
        )

        fun deserialize(raw: String): NoteCardColor =
            deserializeOrNull(raw) ?: Default

        private fun deserializeOrNull(raw: String): NoteCardColor? {
            legacyNames[raw]?.let { return it }
            val hex = raw.removePrefix("#")
            if (hex.length != 8) return null
            val argb = hex.toLongOrNull(16) ?: return null
            return NoteCardColor(Color(argb))
        }

        /** Restores favorites from stored values; unknown entries are dropped. */
        fun favoritesFrom(raw: List<String>): List<NoteCardColor> =
            raw.mapNotNull(::deserializeOrNull).ifEmpty { DefaultFavorites }
    }
}

private fun Color.toArgbLong(): Long {
    fun channel(v: Float): Long = (v * 255f + 0.5f).toLong().coerceIn(0L, 255L)
    return (channel(alpha) shl 24) or (channel(red) shl 16) or (channel(green) shl 8) or channel(blue)
}

// region Derived palettes for user-picked colors

private fun derivedPalette(seed: Color): NoteCardPalette {
    val (h, s, l) = seed.toHsl()
    val textSat = (s * 0.9f).coerceIn(0.25f, 0.85f)
    return if (l > 0.5f) {
        NoteCardPalette(
            background = seed,
            title = Color.hsl(h, textSat, 0.18f),
            body = Color.hsl(h, textSat, 0.32f),
            date = Color.hsl(h, textSat, 0.42f),
        )
    } else {
        NoteCardPalette(
            background = seed,
            title = Color.hsl(h, (s * 0.6f).coerceAtMost(0.9f), 0.95f),
            body = Color.hsl(h, (s * 0.7f).coerceAtMost(0.9f), 0.82f),
            date = Color.hsl(h, (s * 0.7f).coerceAtMost(0.9f), 0.74f),
        )
    }
}

/** RGB → HSL; hue in degrees [0, 360), saturation and lightness in [0, 1]. */
private fun Color.toHsl(): Triple<Float, Float, Float> {
    val max = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val delta = max - min
    val l = (max + min) / 2f
    if (delta == 0f) return Triple(0f, 0f, l)
    val s = delta / (1f - kotlin.math.abs(2f * l - 1f))
    val h = when (max) {
        red -> ((green - blue) / delta).mod(6f)
        green -> (blue - red) / delta + 2f
        else -> (red - green) / delta + 4f
    } * 60f
    return Triple(h.mod(360f), s.coerceIn(0f, 1f), l)
}

// endregion

// region Hand-tuned preset palettes

private val presetPalettes: Map<Color, NoteCardPalette> = mapOf(
    NoteCardColor.Purple.seed to NoteCardPalette(
        background = Color(0xFFCECBF6),
        title = Color(0xFF26215C),
        body = Color(0xFF3C3489),
        date = Color(0xFF534AB7),
    ),
    NoteCardColor.Pink.seed to NoteCardPalette(
        background = Color(0xFFF4C0D1),
        title = Color(0xFF4B1528),
        body = Color(0xFF993556),
        date = Color(0xFF993556),
    ),
    NoteCardColor.Green.seed to NoteCardPalette(
        background = Color(0xFF9FE1CB),
        title = Color(0xFF04342C),
        body = Color(0xFF085041),
        date = Color(0xFF0F6E56),
    ),
    NoteCardColor.Orange.seed to NoteCardPalette(
        background = Color(0xFFFAC775),
        title = Color(0xFF412402),
        body = Color(0xFF633806),
        date = Color(0xFF854F0B),
    ),
    NoteCardColor.Blue.seed to NoteCardPalette(
        background = Color(0xFFBBD6F6),
        title = Color(0xFF122A4B),
        body = Color(0xFF1F4479),
        date = Color(0xFF2C5CA5),
    ),
    NoteCardColor.Teal.seed to NoteCardPalette(
        background = Color(0xFFA8E3E0),
        title = Color(0xFF063736),
        body = Color(0xFF0B5654),
        date = Color(0xFF117573),
    ),
    NoteCardColor.Red.seed to NoteCardPalette(
        background = Color(0xFFF6BFB7),
        title = Color(0xFF4A150E),
        body = Color(0xFF8E2F23),
        date = Color(0xFF8E2F23),
    ),
    NoteCardColor.Yellow.seed to NoteCardPalette(
        background = Color(0xFFEDE28B),
        title = Color(0xFF3A3403),
        body = Color(0xFF5C5307),
        date = Color(0xFF7A6E0C),
    ),
)

// endregion
