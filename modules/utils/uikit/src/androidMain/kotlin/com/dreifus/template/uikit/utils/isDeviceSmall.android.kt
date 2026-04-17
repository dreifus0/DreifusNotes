package com.dreifus.template.uikit.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
actual fun getSmallestScreenWidthDp(): Int {
    return LocalConfiguration.current.smallestScreenWidthDp
}
