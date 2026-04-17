package com.dreifus.template.uikit.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme

@Composable
fun CloseIcon(
    onCloseClick: () -> Unit,
    isShapeVisible: Boolean = true,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, end = 24.dp),
        contentAlignment = Alignment.TopEnd,
    ) {
        AppIcons.Close24(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable { onCloseClick() }
                .testTag("close_bottom_sheet")
                .run {
                    if (isShapeVisible) {
                        this.background(
                            color = AppTheme.colors.backgroundSecondary,
                            shape = CircleShape
                        )
                    } else {
                        this
                    }
                }
                .padding(6.dp),
        )
    }
}
