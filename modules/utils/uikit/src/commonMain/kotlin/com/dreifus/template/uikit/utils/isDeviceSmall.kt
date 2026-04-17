package com.dreifus.template.uikit.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * These functions check the smallest screen width.
 * On iOS they default to false (assume non-small device).
 * For precise checks, use BoxWithConstraints at call site.
 */
@Composable
expect fun getSmallestScreenWidthDp(): Int

@Composable
fun isDeviceSmall(): Boolean {
    return getSmallestScreenWidthDp() <= 360
}

@Composable
fun isDeviceSmallerThan380dp(): Boolean {
    return getSmallestScreenWidthDp() < 380
}

@Composable
fun isDeviceSmallerThanOrEqual320dp(): Boolean {
    return getSmallestScreenWidthDp() <= 320
}
