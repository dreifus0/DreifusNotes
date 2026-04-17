package com.dreifus.navigation.screen.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.scene.DialogSceneStrategy
import com.dreifus.navigation.IInsetsConsumer
import com.dreifus.navigation.imePaddingIfNeeded
import com.dreifus.navigation.navigationBarsPaddingIfNeeded
import com.dreifus.navigation.screen.BaseScreen
import com.dreifus.navigation.statusBarsPaddingIfNeeded
import com.dreifus.template.uikit.style.AppTheme

interface DialogScreen : BaseScreen {
    override fun <T : BaseScreen> navEntry() = navEntry<T>(
        metadata = DialogSceneStrategy.dialog(
            DialogProperties(
                usePlatformDefaultWidth = true,
            )
        ),
    ) { screen ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.contentPrimary.copy(alpha = 0.3f))
                .testTag(screen::class.simpleName!!)
                .statusBarsPaddingIfNeeded(screen as? IInsetsConsumer)
                .imePaddingIfNeeded(screen as? IInsetsConsumer)
                .navigationBarsPaddingIfNeeded(screen as? IInsetsConsumer),
            contentAlignment = Alignment.Companion.Center,
        ) {
            screen.Content()
        }
    }
}
