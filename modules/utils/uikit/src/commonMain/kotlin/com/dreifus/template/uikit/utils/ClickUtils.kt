package com.dreifus.template.uikit.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import kotlinx.datetime.Clock

private const val DOUBLE_CLICK_DEFENCE_DEFAULT_TIMEOUT = 300L
private var lastClickTimeMillis: Long = 0L

fun doubleClickDefence(onClick: () -> Unit) {
    val now = Clock.System.now().toEpochMilliseconds()
    if (lastClickTimeMillis + DOUBLE_CLICK_DEFENCE_DEFAULT_TIMEOUT > now) {
        return
    }
    lastClickTimeMillis = now
    onClick()
}

@Composable
fun Modifier.clickableWithAlphaIndication(
    isEnabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val alpha = if (isPressed) 0.6f else 1f
    return clickable(
        interactionSource,
        indication = null,
        enabled = isEnabled,
        onClick = onClick,
    ).alpha(alpha)
}
