package com.dreifus.template.uikit.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.dreifus.template.uikit.style.app.DefaultAppTheme
import com.dreifus.template.uikit.style.app.SpecificAppTheme

@Composable
internal fun PreviewTheme(
    appTheme: SpecificAppTheme = DefaultAppTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    appTheme(darkTheme, content)
}
