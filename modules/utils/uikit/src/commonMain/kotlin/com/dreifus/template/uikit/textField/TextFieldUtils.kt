package com.dreifus.template.uikit.textField

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun debounced(timeout: Long = 300L, block: () -> Unit): () -> Unit {
    val state = remember { DebounceState() }
    return {
        val time = System.currentTimeMillis()
        if (state.lastCalled + timeout < time) {
            state.lastCalled = time
            block()
        }
    }
}

@Composable
fun <T> debouncedValueChange(
    debounceTimeMillis: Long = 300L,
    onValueChange: (T) -> Unit,
    debouncedOnValueChange: (T) -> Unit,
): (T) -> Unit {
    val jobState = remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    return {
        jobState.value?.cancel()
        jobState.value = coroutineScope.launch {
            delay(debounceTimeMillis)
            debouncedOnValueChange(it)
        }
        onValueChange(it)
    }
}

private class DebounceState {
    var lastCalled: Long = 0
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = keypadHeight > screenHeight * 0.15
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

fun Modifier.requestFocus(awaitFrame: Boolean = false): Modifier = composed {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (awaitFrame) {
            awaitFrame()
        }
        focusRequester.requestFocus()
    }
    focusRequester(focusRequester)
}

