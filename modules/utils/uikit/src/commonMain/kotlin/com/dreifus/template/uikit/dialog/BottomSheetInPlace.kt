package com.dreifus.template.uikit.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.preview.PreviewTheme
import com.dreifus.template.uikit.style.AppTheme
import kotlinx.coroutines.coroutineScope

@Composable
fun BottomSheetInPlace(
    state: BottomSheetInPlaceState,
    modifier: Modifier = Modifier,
    draggableSpaceHeight: Dp = 60.dp,
    visible: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        LaunchedEffect(maxHeight) {
            state.containerHeight = maxHeight.value
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideIn { IntOffset(0, it.height) },
            exit = slideOut { IntOffset(0, it.height) },
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 34.dp,
                        shape = AppTheme.shapes.bottomSheet,
                    )
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(state.height.dp)
                    .clip(AppTheme.shapes.bottomSheet)
                    .background(AppTheme.colors.backgroundBase)
                    .border(1.dp, AppTheme.colors.contentBorder, AppTheme.shapes.bottomSheet)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(draggableSpaceHeight)
                        .draggable(
                            state,
                            orientation = Orientation.Vertical,
                            onDragStopped = { velocity ->
                                state.onStop(velocity)
                            }
                        )
                )
                content()
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 4.dp)
                        .size(60.dp, 4.dp)
                        .background(AppTheme.colors.contentDividers, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Stable
class BottomSheetInPlaceState(
    private val density: Density,
    private val minHeight: Dp,
    private val maxHeight: Dp = Dp.Infinity,
    initialHeight: Dp = minHeight,
) : DraggableState {
    private val swipeVelocityThreshold = with(density) { 125.dp.toPx() }

    private var internalHeight = maxOf(initialHeight.value, minHeight.value)
    var height by mutableFloatStateOf(internalHeight)
        private set

    internal var containerHeight: Float = Float.POSITIVE_INFINITY

    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = dispatchRawDelta(pixels)
    }

    private val scrollMutex = MutatorMutex()
    val isSwiped = mutableStateOf(false)

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit,
    ): Unit = coroutineScope {
        scrollMutex.mutateWith(dragScope, dragPriority, block)
    }

    override fun dispatchRawDelta(delta: Float) {
        isSwiped.value = false
        internalHeight -= delta / density.density
        val max = minOf(containerHeight, maxHeight.value)
        val min = minOf(containerHeight, minHeight.value)
        height = internalHeight.coerceIn(min, max)
    }

    internal fun onStop(velocity: Float) {
        if (velocity > swipeVelocityThreshold || internalHeight < minHeight.value * 0.6) {
            isSwiped.value = true
        }
        internalHeight = height // reset overscroll on drag stop
    }
}

@Composable
fun rememberBottomSheetInPlaceState(
    minHeight: Dp,
    maxHeight: Dp = Dp.Infinity,
    initialHeight: Dp = minHeight,
): BottomSheetInPlaceState {
    val density = LocalDensity.current
    return remember { BottomSheetInPlaceState(density, minHeight, maxHeight, initialHeight) }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    PreviewTheme {
        val state = rememberBottomSheetInPlaceState(
            minHeight = 300.dp,
        )
        var visible by remember { mutableStateOf(true) }
        Button(onClick = { visible = !visible }) {
            Text("toggle")
        }
        BottomSheetInPlace(
            state = state,
            visible = visible,
        ) {
            Column {
                repeat(30) {
                    Text(text = "item $it")
                }
            }
        }
    }
}
