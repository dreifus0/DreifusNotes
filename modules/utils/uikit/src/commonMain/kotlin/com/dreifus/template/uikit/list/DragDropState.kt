package com.dreifus.template.uikit.list

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * Long-press drag & drop reordering for a `LazyColumn`.
 *
 * Wiring: put [dragContainer] on the `LazyColumn`, [draggableItemModifier] on each reorderable
 * item, and reorder the backing list in [onMove] ([LazyListState] indices — subtract any header
 * items). Items whose [canDrag] is false (headers etc.) can be neither dragged nor displaced.
 */
@Composable
fun rememberDragDropState(
    lazyListState: LazyListState,
    canDrag: (LazyListItemInfo) -> Boolean = { true },
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    onDragEnd: () -> Unit = {},
): DragDropState {
    val scope = rememberCoroutineScope()
    val state = remember(lazyListState) {
        DragDropState(lazyListState, scope, canDrag, onMove, onDragEnd)
    }
    LaunchedEffect(state) {
        while (true) {
            val diff = state.scrollChannel.receive()
            lazyListState.scrollBy(diff)
        }
    }
    return state
}

class DragDropState internal constructor(
    private val state: LazyListState,
    private val scope: CoroutineScope,
    private val canDrag: (LazyListItemInfo) -> Boolean,
    private val onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    private val onDragEnd: () -> Unit,
) {
    var draggingItemIndex by mutableStateOf<Int?>(null)
        private set
    var previousIndexOfDraggedItem by mutableStateOf<Int?>(null)
        private set
    val previousItemOffset = Animatable(0f)

    internal val scrollChannel = Channel<Float>()

    private var draggingItemDraggedDelta by mutableStateOf(0f)
    private var draggingItemInitialOffset by mutableStateOf(0)

    val draggingItemOffset: Float
        get() = draggingItemLayoutInfo?.let { item ->
            draggingItemInitialOffset + draggingItemDraggedDelta - item.offset
        } ?: 0f

    private val draggingItemLayoutInfo: LazyListItemInfo?
        get() = state.layoutInfo.visibleItemsInfo.firstOrNull { it.index == draggingItemIndex }

    internal fun onDragStart(offset: Offset) {
        state.layoutInfo.visibleItemsInfo
            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
            ?.takeIf(canDrag)
            ?.also { item ->
                draggingItemIndex = item.index
                draggingItemInitialOffset = item.offset
            }
    }

    internal fun onDragInterrupted() {
        val dragged = draggingItemIndex
        if (dragged != null) {
            // Settle the released card into its slot instead of snapping.
            previousIndexOfDraggedItem = dragged
            val startOffset = draggingItemOffset
            scope.launch {
                previousItemOffset.snapTo(startOffset)
                previousItemOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow, visibilityThreshold = 1f),
                )
                previousIndexOfDraggedItem = null
            }
            onDragEnd()
        }
        draggingItemDraggedDelta = 0f
        draggingItemIndex = null
        draggingItemInitialOffset = 0
    }

    internal fun onDrag(offset: Offset) {
        draggingItemDraggedDelta += offset.y

        val draggingItem = draggingItemLayoutInfo ?: return
        val startOffset = draggingItem.offset + draggingItemOffset
        val endOffset = startOffset + draggingItem.size
        val middleOffset = startOffset + (endOffset - startOffset) / 2f

        val targetItem = state.layoutInfo.visibleItemsInfo.find { item ->
            middleOffset.toInt() in item.offset..(item.offset + item.size) &&
                item.index != draggingItem.index &&
                canDrag(item)
        }
        if (targetItem != null) {
            // Keep the viewport anchored when the first visible item takes part in the swap,
            // otherwise LazyColumn scrolls to follow it.
            if (draggingItem.index == state.firstVisibleItemIndex ||
                targetItem.index == state.firstVisibleItemIndex
            ) {
                state.requestScrollToItem(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset)
            }
            onMove(draggingItem.index, targetItem.index)
            draggingItemIndex = targetItem.index
        } else {
            val overscroll = when {
                draggingItemDraggedDelta > 0 ->
                    (endOffset - state.layoutInfo.viewportEndOffset).coerceAtLeast(0f)
                draggingItemDraggedDelta < 0 ->
                    (startOffset - state.layoutInfo.viewportStartOffset).coerceAtMost(0f)
                else -> 0f
            }
            if (overscroll != 0f) scrollChannel.trySend(overscroll)
        }
    }
}

fun Modifier.dragContainer(dragDropState: DragDropState): Modifier =
    pointerInput(dragDropState) {
        detectDragGesturesAfterLongPress(
            onDragStart = dragDropState::onDragStart,
            onDrag = { change, offset ->
                change.consume()
                dragDropState.onDrag(offset)
            },
            onDragEnd = dragDropState::onDragInterrupted,
            onDragCancel = dragDropState::onDragInterrupted,
        )
    }

/** Modifier for a reorderable item's content; [index] is the item's [LazyListState] index. */
@Composable
fun LazyItemScope.draggableItemModifier(dragDropState: DragDropState, index: Int): Modifier =
    when (index) {
        dragDropState.draggingItemIndex -> Modifier
            .zIndex(1f)
            .graphicsLayer { translationY = dragDropState.draggingItemOffset }
        dragDropState.previousIndexOfDraggedItem -> Modifier
            .zIndex(1f)
            .graphicsLayer { translationY = dragDropState.previousItemOffset.value }
        else -> Modifier.animateItem()
    }
