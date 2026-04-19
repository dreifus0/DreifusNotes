package com.dreifus.app.features.pin.lock

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.pin.lock.mvu.PinLockEffect
import com.dreifus.app.features.pin.lock.mvu.PinLockEvent
import com.dreifus.app.features.pin.lock.mvu.PinLockState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.icon.GlassIcon
import com.dreifus.template.uikit.icon.Lock24
import com.dreifus.template.uikit.pin.PinDots
import com.dreifus.template.uikit.pin.PinKeypad
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import dev.zacsweers.metrox.viewmodel.metroViewModel

class PinLockScreen(
    private val noteId: Long,
    private val onUnlocked: (noteId: Long, pin: String) -> Unit,
) : RegularScreen {

    @Composable
    override fun Content() {
        val vm = metroViewModel<PinLockViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val regularNav = Navigation.regular

        LaunchedEffect(Unit) {
            vm.dispatch(PinLockEvent.Ui.Init(noteId))
        }
        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    is PinLockEffect.NavigateToNote -> onUnlocked(effect.noteId, effect.pin)
                    PinLockEffect.NavigateBack -> regularNav.pop()
                }
            }
        }

        PinLockContent(state = state, onEvent = vm::dispatch)
    }
}

@Composable
private fun PinLockContent(
    state: PinLockState,
    onEvent: (PinLockEvent.Ui) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().background(AppTheme.colors.backgroundBase)) {
        GlassIcon(
            icon = AppIcons.ArrowLeft24,
            onClick = { onEvent(PinLockEvent.Ui.BackClick) },
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 28.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val iconBg = AppTheme.colors.accentSecondary.copy(alpha = 0.18f)
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                AppIcons.Lock24(tint = AppTheme.colors.accentPrimary)
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text = "Enter passcode",
                style = AppTheme.typography.heading4,
                color = AppTheme.colors.contentPrimary,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "This note is locked. Enter your 4-digit\npasscode to view it.",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.contentSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(32.dp))

            val dotColor: Color by animateColorAsState(
                targetValue = if (state.isError) AppTheme.colors.accentError else AppTheme.colors.accentPrimary,
                animationSpec = tween(200),
                label = "dotColor",
            )
            PinDots(
                filledCount = if (state.isError) 4 else state.enteredPin.length,
                dotColor = dotColor,
            )

            Spacer(Modifier.height(48.dp))
        }

        PinKeypad(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            onKey = { onEvent(PinLockEvent.Ui.KeyPress(it)) },
            onBackspace = { onEvent(PinLockEvent.Ui.Backspace) },
        )
    }
}

@Preview
@Composable
private fun PreviewPinLock() {
    AppPreview {
        PinLockContent(
            state = PinLockState(enteredPin = "123"),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewPinLockError() {
    AppPreview {
        PinLockContent(
            state = PinLockState(enteredPin = "", isError = true),
            onEvent = {},
        )
    }
}
