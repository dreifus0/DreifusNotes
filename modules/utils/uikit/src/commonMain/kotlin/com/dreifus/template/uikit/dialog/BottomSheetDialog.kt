@file:OptIn(ExperimentalMaterial3Api::class)

package com.dreifus.template.uikit.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.icon.CloseIcon
import com.dreifus.template.uikit.scroll.rememberScrollDetectorConnection
import com.dreifus.template.uikit.style.AppTheme
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@Composable
fun BottomSheetDialog(
    isVisible: Boolean = true,
    showCloseButton: Boolean = true,
    scrimColor: Color = Color(0x52000000),
    background: Color = AppTheme.colors.backgroundBase,
    onDismissed: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    var internalVisible by remember { mutableStateOf(isVisible) }
    var dissmissing by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (isVisible) {
            internalVisible = true
            dissmissing = false
        }
    }
    if (!internalVisible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val show = isVisible && !dissmissing
    LaunchedEffect(show) {
        if (show) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    if (sheetState.isDisplayed) {
        DisposableEffect(Unit) {
            onDispose {
                if (!sheetState.isDisplayed) {
                    onDismissed()
                    internalVisible = false
                }
            }
        }
    }
    val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    BoxWithConstraints {
        val isPortrait = maxWidth < maxHeight
        val sheetMaxWidth = if (isPortrait) maxWidth else maxHeight

        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            scrimColor = scrimColor,
            dragHandle = {},
            contentWindowInsets = { WindowInsets.ime },
            sheetMaxWidth = sheetMaxWidth,
            content = {
                val scrollDetectorConnection = rememberScrollDetectorConnection()
                Box(
                    modifier = Modifier
                        .padding(statusBarPadding)
                        .background(background, AppTheme.shapes.bottomSheet)
                        .clip(AppTheme.shapes.bottomSheet)
                        .run {
                            if (AppTheme.colors.isDarkTheme) {
                                this.border(
                                    1.dp,
                                    AppTheme.colors.contentBorder,
                                    AppTheme.shapes.bottomSheet
                                )
                            } else {
                                this
                            }
                        }
                        .nestedScroll(scrollDetectorConnection)
                        .let {
                            if (isPortrait) {
                                it.padding(navigationBarsPadding)
                            } else {
                                it
                            }
                        }
                ) {
                    content()
                    if (showCloseButton) {
                        val scope = rememberCoroutineScope()
                        CloseIcon(
                            onCloseClick = { scope.launch { sheetState.hide() } },
                            isShapeVisible = scrollDetectorConnection.isScrolled,
                        )
                    }
                }
            },
        )
    }
}

private val SheetState.isDisplayed get() = isVisible || targetValue == SheetValue.Expanded
