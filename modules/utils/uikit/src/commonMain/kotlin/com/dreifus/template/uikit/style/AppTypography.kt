package com.dreifus.template.uikit.style

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

val FontFamily = FontFamily.Default

private val defaultLetterSpacing: TextUnit = (-0.007).em

@Immutable
class AppTypography(
    private val fontFamily: FontFamily,
) {
    private val regular = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = defaultLetterSpacing,
    )
    private val semiBold = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W600,
        letterSpacing = defaultLetterSpacing,
    )

    val heading1: TextStyle = semiBold.copy(fontSize = 40.sp, lineHeight = 48.sp)
    val heading2: TextStyle = semiBold.copy(fontSize = 32.sp, lineHeight = 40.sp)
    val heading3: TextStyle = semiBold.copy(fontSize = 28.sp, lineHeight = 32.sp)
    val heading4: TextStyle = semiBold.copy(fontSize = 24.sp, lineHeight = 28.sp)
    val heading5: TextStyle = semiBold.copy(fontSize = 20.sp, lineHeight = 24.sp)

    val headlineLarge: TextStyle = semiBold.copy(fontSize = 16.sp, lineHeight = 20.sp)
    val headlineMedium: TextStyle = semiBold.copy(fontSize = 14.sp, lineHeight = 18.sp)
    val headlineSmall: TextStyle = semiBold.copy(fontSize = 12.sp, lineHeight = 16.sp)

    val bodyLarge: TextStyle = regular.copy(fontSize = 16.sp, lineHeight = 24.sp)
    val bodyMedium: TextStyle = regular.copy(fontSize = 14.sp, lineHeight = 20.sp)
    val bodySmall: TextStyle = regular.copy(fontSize = 12.sp, lineHeight = 16.sp)

    fun toComposeTypography(): Typography {
        return Typography().let {
            it.copy(
                displayLarge = it.displayLarge.copy(fontFamily = fontFamily),
                displayMedium = it.displayMedium.copy(fontFamily = fontFamily),
                displaySmall = it.displaySmall.copy(fontFamily = fontFamily),
                headlineLarge = it.headlineLarge.copy(fontFamily = fontFamily),
                headlineMedium = it.headlineMedium.copy(fontFamily = fontFamily),
                headlineSmall = it.headlineSmall.copy(fontFamily = fontFamily),
                titleLarge = it.titleLarge.copy(fontFamily = fontFamily),
                titleMedium = it.titleMedium.copy(fontFamily = fontFamily),
                titleSmall = it.titleSmall.copy(fontFamily = fontFamily),
                bodyLarge = it.bodyLarge.copy(fontFamily = fontFamily),
                bodyMedium = it.bodyMedium.copy(fontFamily = fontFamily),
                bodySmall = it.bodySmall.copy(fontFamily = fontFamily),
                labelLarge = it.labelLarge.copy(fontFamily = fontFamily),
                labelMedium = it.labelMedium.copy(fontFamily = fontFamily),
                labelSmall = it.labelSmall.copy(fontFamily = fontFamily)
            )
        }
    }
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        FontFamily.Default,
    )
}
