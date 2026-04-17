package com.dreifus.template.uikit.toolbar

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.debug.LogCompositions
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.preview.PreviewTheme
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import androidx.compose.foundation.verticalScroll as originVerticalScroll

private val emptyAction = {}

@Composable
fun CollapsingToolbarScaffold(
    title: String,
    modifier: Modifier = Modifier,
    button: ToolbarButton? = null,
    startIcon: Painter? = if (button?.position == ToolbarPosition.START) button.icon.painter else null,
    startIconClick: () -> Unit = if (button?.position == ToolbarPosition.START) button.onClick() else emptyAction,
    endIcon: Painter? = if (button?.position == ToolbarPosition.END) button.icon.painter else null,
    endIconClick: () -> Unit = if (button?.position == ToolbarPosition.END) button.onClick() else emptyAction,
    childScrollableState: ScrollableState? = null,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable CollapsingToolbarScope.(PaddingValues) -> Unit
) {
    val connection = rememberCollapsingConnection(childScrollableState)
    val showSmallTitle by remember(connection) {
        derivedStateOf {
            connection.bigTitleOffset >= connection.bigTitleHeight
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(connection),
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        topBar = {
            Column {
                AppToolbar(
                    title = title,
                    modifier = modifier,
                    startIcon = startIcon,
                    startIconClick = startIconClick,
                    endIcon = endIcon,
                    endIconClick = endIconClick,
                    titleVisible = showSmallTitle,
                )
                CollapsingTitle(title, connection)
            }
        },
        content = {
            connection.content(it)
        },
    )
}

@Composable
private fun CollapsingTitle(title: String, connection: CollapsingConnection) {
    Layout(
        content = {
            Text(
                text = title,
                style = AppTheme.typography.heading4,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )
        },
        modifier = Modifier.clip(RectangleShape)
    ) { measurables, constraints ->
        val placeables = measurables.map {
            it.measure(constraints)
        }
        val width = placeables.maxOf { it.width }
        val height = placeables.maxOf { it.height }
        connection.bigTitleHeight = height.toFloat()
        val offset = connection.bigTitleOffset.roundToInt()
        layout(width, height - offset) {
            for (placeable in placeables) {
                placeable.place(0, -offset)
            }
        }
    }
}

@Stable
private class CollapsingConnection(
    private var childScrollableState: ScrollableState? = null,
    private val flingBehavior: FlingBehavior,
) : NestedScrollConnection, CollapsingToolbarScope {

    var bigTitleHeight by mutableFloatStateOf(1f)
    var bigTitleOffset by mutableFloatStateOf(0f)

    private val childCanScroll
        get() = childScrollableState.let { it == null || it.canScrollForward || it.canScrollBackward }

    private val scrollableState = ScrollableState { value ->
        val consume = if (value < 0) {
            max(bigTitleOffset - bigTitleHeight, value)
        } else {
            min(bigTitleOffset, value)
        }

        bigTitleOffset -= consume
        consume
    }


    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        return if (available.y < 0f && childCanScroll) {
            Offset(0f, scrollableState.dispatchRawDelta(available.y))
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        return if (available.y > 0f && source == NestedScrollSource.Drag) {
            Offset(0f, scrollableState.dispatchRawDelta(available.y))
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        return if (available.y <= 0f && childCanScroll) {
            Velocity(x = 0f, y = available.y - flingBigTitle(available.y))
        } else {
            Velocity.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        return if (available.y > 0f) {
            Velocity(x = 0f, y = available.y - flingBigTitle(available.y))
        } else {
            Velocity.Zero
        }
    }

    private suspend fun flingBigTitle(availableY: Float): Float {
        var left = availableY
        scrollableState.scroll {
            with(flingBehavior) {
                left = performFling(left)
            }
        }
        val target = when {
            availableY > 0f -> 0f
            availableY < 0f -> bigTitleHeight
            bigTitleOffset / bigTitleHeight < 0.5f -> 0f
            else -> bigTitleHeight
        }
        if (target != bigTitleOffset) {
            Animatable(bigTitleOffset).animateTo(target) {
                bigTitleOffset = value
            }
        }
        return left
    }

    override fun Modifier.verticalScroll(
        state: ScrollState,
        enabled: Boolean,
        flingBehavior: FlingBehavior?,
        reverseScrolling: Boolean
    ): Modifier {
        childScrollableState = state
        return this.originVerticalScroll(state, enabled, flingBehavior, reverseScrolling)
    }
}

@Composable
private fun rememberCollapsingConnection(
    childScrollableState: ScrollableState? = null,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
): CollapsingConnection {
    return remember(flingBehavior, childScrollableState) { CollapsingConnection(childScrollableState, flingBehavior) }
}

interface CollapsingToolbarScope {
    fun Modifier.verticalScroll(
        state: ScrollState,
        enabled: Boolean = true,
        flingBehavior: FlingBehavior? = null,
        reverseScrolling: Boolean = false
    ): Modifier
}

@Preview(device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun CollapsingToolbarScaffoldPreview() {
    PreviewTheme {
        CollapsingToolbarScaffold(
            title = "Deposit",
            startIcon = AppIcons.ArrowLeft24.painter,
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                ScrollContent()
            }
        }
    }
}

@Composable
private fun ScrollContent() {
    LogCompositions("Recomposition", "ScrollContent")
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(30) {
            Text(text = "item $it")
        }
    }
}
