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
 * A note's accent color, seeded by a single [seed] color. The eight built-in presets keep their
 * hand-tuned light/dark palettes; any other (user-picked) seed gets a palette derived from its hue.
 */
@Immutable
data class NoteCardColor(val seed: Color) {

    @Composable
    fun palette(): NoteCardPalette {
        val isDark = AppTheme.colors.isDarkTheme
        val preset = presetPalettes[seed]
        return when {
            preset != null -> if (isDark) preset.dark else preset.light
            isDark -> derivedDarkPalette(seed)
            else -> derivedLightPalette(seed)
        }
    }

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

private class PresetPalette(val light: NoteCardPalette, val dark: NoteCardPalette)

// region Derived palettes for user-picked colors

private fun derivedLightPalette(seed: Color): NoteCardPalette {
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

private fun derivedDarkPalette(seed: Color): NoteCardPalette {
    val (h, s, _) = seed.toHsl()
    val bgSat = (s * 0.85f).coerceIn(0.2f, 0.7f)
    return NoteCardPalette(
        background = Color.hsl(h, bgSat, 0.30f),
        title = Color.hsl(h, (s * 0.6f).coerceAtMost(0.9f), 0.94f),
        body = Color.hsl(h, (s * 0.75f).coerceAtMost(0.9f), 0.72f),
        date = Color.hsl(h, (s * 0.75f).coerceAtMost(0.9f), 0.72f),
    )
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

private val presetPalettes: Map<Color, PresetPalette> = mapOf(
    NoteCardColor.Purple.seed to PresetPalette(
        light = NoteCardPalette(
            background = Color(0xFFCECBF6),
            title = Color(0xFF26215C),
            body = Color(0xFF3C3489),
            date = Color(0xFF534AB7),
        ),
        dark = NoteCardPalette(
            background = Color(0xFF3C3489),
            title = Color(0xFFEEEDFE),
            body = Color(0xFFAFA9EC),
            date = Color(0xFFAFA9EC),
        ),
    ),
    NoteCardColor.Pink.seed to PresetPalette(
        light = NoteCardPalette(
            background = Color(0xFFF4C0D1),
            title = Color(0xFF4B1528),
            body = Color(0xFF993556),
            date = Color(0xFF993556),
        ),
        dark = NoteCardPalette(
            background = Color(0xFF72243E),
            title = Color(0xFFFBEAF0),
            body = Color(0xFFED93B1),
            date = Color(0xFFED93B1),
        ),
    ),
    NoteCardColor.Green.seed to PresetPalette(
        light = NoteCardPalette(
            background = Color(0xFF9FE1CB),
            title = Color(0xFF04342C),
            body = Color(0xFF085041),
            date = Color(0xFF0F6E56),
        ),
        dark = NoteCardPalette(
            background = Color(0xFF085041),
            title = Color(0xFFE1F5EE),
            body = Color(0xFF5DCAA5),
            date = Color(0xFF5DCAA5),
        ),
    ),
    NoteCardColor.Orange.seed to PresetPalette(
        light = NoteCardPalette(
            background = Color(0xFFFAC775),
            title = Color(0xFF412402),
            body = Color(0xFF633806),
            date = Color(0xFF854F0B),
        ),
        dark = NoteCardPalette(
            background = Color(0xFF633806),
            title = Color(0xFFFAEEDA),
            body = Color(0xFFEF9F27),
            date = Color(0xFFEF9F27),
        ),
    ),
    NoteCardColor.Blue.seed to PresetPalette(
        light = NoteCardPalette(
            background = Color(0xFFBBD6F6),
            title = Color(0xFF122A4B),
            body = Color(0xFF1F4479),
            date = Color(0xFF2C5CA5),
        ),
        dark = NoteCardPalette(
            background = Color(0xFF1F4479),
            title = Color(0xFFE9F1FD),
            body = Color(0xFF8FB8ED),
            date = Color(0xFF8FB8ED),
        ),
    ),
    NoteCardColor.Teal.seed to PresetPalette(
        light = NoteCardPalette(
            background = Color(0xFFA8E3E0),
            title = Color(0xFF063736),
            body = Color(0xFF0B5654),
            date = Color(0xFF117573),
        ),
        dark = NoteCardPalette(
            background = Color(0xFF0B5654),
            title = Color(0xFFE2F6F5),
            body = Color(0xFF63CFCB),
            date = Color(0xFF63CFCB),
        ),
    ),
    NoteCardColor.Red.seed to PresetPalette(
        light = NoteCardPalette(
            background = Color(0xFFF6BFB7),
            title = Color(0xFF4A150E),
            body = Color(0xFF8E2F23),
            date = Color(0xFF8E2F23),
        ),
        dark = NoteCardPalette(
            background = Color(0xFF6E241A),
            title = Color(0xFFFBECEA),
            body = Color(0xFFEC948A),
            date = Color(0xFFEC948A),
        ),
    ),
    NoteCardColor.Yellow.seed to PresetPalette(
        light = NoteCardPalette(
            background = Color(0xFFEDE28B),
            title = Color(0xFF3A3403),
            body = Color(0xFF5C5307),
            date = Color(0xFF7A6E0C),
        ),
        dark = NoteCardPalette(
            background = Color(0xFF5C5307),
            title = Color(0xFFF7F3DC),
            body = Color(0xFFD8CA45),
            date = Color(0xFFD8CA45),
        ),
    ),
)

// endregion
