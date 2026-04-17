package com.dreifus.template.uikit.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dreifus.template.uikit.icon.CloseIcon
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.app.DefaultAppTheme
import com.dreifus.template.uikit.style.app.SpecificAppTheme

@Composable
fun AppPreview(
    appTheme: SpecificAppTheme = DefaultAppTheme,
    useSurfaceAsBackground: Boolean = true,
    content: @Composable () -> Unit,
) {
    PreviewTheme(
        appTheme = appTheme,
    ) {
        if (useSurfaceAsBackground) {
            Surface {
                content()
            }
        }
    }
}

@Composable
fun AppBottomSheetPreview(
    appTheme: SpecificAppTheme = DefaultAppTheme,
    fullScreen: Boolean = false,
    content: @Composable () -> Unit,
) {
    PreviewTheme(
        appTheme = appTheme,
    ) {
        Box(
            modifier = Modifier
                .let { if (fullScreen) it.fillMaxSize() else it }
                .background(AppTheme.colors.backgroundDisabled),
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shape = AppTheme.shapes.bottomSheet,
                color = AppTheme.colors.backgroundBase,
            ) {
                content()
                CloseIcon(onCloseClick = {}, isShapeVisible = false)
            }
        }
    }
}
