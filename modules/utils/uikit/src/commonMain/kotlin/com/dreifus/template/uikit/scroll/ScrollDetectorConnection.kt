package com.dreifus.template.uikit.scroll

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import kotlin.math.absoluteValue

class ScrollDetectorConnection : NestedScrollConnection {
    private val offsetHeightPx = mutableFloatStateOf(0f)

    // offset won't become 0 after scroll, so better to use a larger value
    val isScrolled: Boolean
        get() = offsetHeightPx.floatValue.absoluteValue > 1f

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = consumed.y
        val newOffset = offsetHeightPx.floatValue + delta
        offsetHeightPx.floatValue = newOffset
        return Offset.Zero
    }
}

@Composable
fun rememberScrollDetectorConnection() = remember { ScrollDetectorConnection() }

