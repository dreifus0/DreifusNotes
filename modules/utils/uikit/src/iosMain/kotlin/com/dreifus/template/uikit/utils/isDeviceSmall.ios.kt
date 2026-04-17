@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.dreifus.template.uikit.utils

import androidx.compose.runtime.Composable
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen

@Composable
actual fun getSmallestScreenWidthDp(): Int {
    val screen = UIScreen.mainScreen
    return screen.bounds.useContents {
        val widthDp = size.width.toInt()
        val heightDp = size.height.toInt()
        minOf(widthDp, heightDp)
    }
}
