package com.dreifus.template.uikit.style.app

import androidx.compose.ui.graphics.Color
import com.dreifus.template.uikit.style.AppColors

object DefaultAppTheme : SpecificAppTheme() {
    override val lightColors = AppColors(
        contentPrimary = Color(0xFF0B0B0C),
        contentSecondary = Color(0xFF5A5A5F),
        contentTertiary = Color(0xFF9A9A9F),
        contentDividers = Color(0xFFE5E3DC),
        contentBorder = Color(0xFFE5E3DC),
        contentShadow = Color(0x14000000),
        backgroundBase = Color(0xFFFFFFFF),
        backgroundSecondary = Color(0xFFF1EFE8),
        backgroundDisabled = Color(0xFFF1EFE8),
        backgroundActive = Color(0x14000000),
        accentPrimary = Color(0xFF534AB7),
        accentSecondary = Color(0xFF534AB7),
        accentError = Color(0xFFA32D2D),
        accentLink = Color(0xFF534AB7),
        accentOnPrimary = Color(0xFFFFFFFF),
        accentOnSecondary = Color(0xFFFFFFFF),
    )

    override val darkColors = AppColors(
        contentPrimary = Color(0xFFEDEDEE),
        contentSecondary = Color(0xFF9898A0),
        contentTertiary = Color(0xFF6A6A70),
        contentDividers = Color(0xFF2A2A2E),
        contentBorder = Color(0xFF2A2A2E),
        contentShadow = Color(0x29000000),
        backgroundBase = Color(0xFF0E0E10),
        backgroundSecondary = Color(0xFF1E1E22),
        backgroundDisabled = Color(0xFF1E1E22),
        backgroundActive = Color(0x1FFFFFFF),
        accentPrimary = Color(0xFF7F77DD),
        accentSecondary = Color(0xFF7F77DD),
        accentError = Color(0xFFE24B4A),
        accentLink = Color(0xFF7F77DD),
        accentOnPrimary = Color(0xFF0E0E10),
        accentOnSecondary = Color(0xFF0E0E10),
    )
}
