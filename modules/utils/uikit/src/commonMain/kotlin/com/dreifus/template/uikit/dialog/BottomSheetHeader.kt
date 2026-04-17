package com.dreifus.template.uikit.dialog

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.utils.isDeviceSmall

@Composable
fun BottomSheetHeader(
    title: String,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 48.dp,
    paddingTop: Dp = 32.dp,
    paddingBottom: Dp = 8.dp,
) {
    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight
        val style =
            if (isDeviceSmall() && isLandscape) {
                AppTheme.typography.headlineLarge
            } else {
                AppTheme.typography.heading5
            }
        Text(
            text = title,
            style = style,
            color = AppTheme.colors.contentPrimary,
            modifier = Modifier.padding(
                start = paddingStart,
                end = paddingEnd,
                top = paddingTop,
                bottom = paddingBottom
            )
        )
    }
}
