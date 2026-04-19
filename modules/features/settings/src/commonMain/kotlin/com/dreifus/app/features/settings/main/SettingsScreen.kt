package com.dreifus.app.features.settings.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.settings.biometric.BiometricAuthEffect
import com.dreifus.app.features.settings.main.mvu.SettingsEffect
import com.dreifus.app.features.settings.main.mvu.SettingsEvent
import com.dreifus.app.features.settings.main.mvu.SettingsState
import com.dreifus.navigation.ui.RootScreenWithTabs
import com.dreifus.template.uikit.glass.glassBorder
import com.dreifus.template.uikit.icon.ChevronRight24
import com.dreifus.template.uikit.icon.Lock24
import com.dreifus.template.uikit.icon.Theme24
import com.dreifus.template.uikit.icon.Trash24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.row.AppSectionLabel
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import dev.zacsweers.metrox.viewmodel.metroViewModel

private val IconShape = RoundedCornerShape(10.dp)

class SettingsScreen : RootScreenWithTabs {

    @Composable
    override fun Content() {
        val vm = metroViewModel<SettingsViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    SettingsEffect.DataReset -> Unit
                }
            }
        }

        BiometricAuthEffect(
            trigger = state.isBiometricPending,
            title = "Confirm reset",
            subtitle = "Authenticate to delete all notes",
            onAuthenticated = { vm.dispatch(SettingsEvent.Ui.BiometricSuccess) },
            onDismissed = { vm.dispatch(SettingsEvent.Ui.BiometricDismissed) },
        )

        SettingsContent(state = state, onEvent = vm::dispatch)
    }
}

@Composable
private fun SettingsContent(
    state: SettingsState,
    onEvent: (SettingsEvent.Ui) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Settings",
            style = AppTheme.typography.heading3,
            color = AppTheme.colors.contentPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 8.dp),
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                AppSectionLabel("General")
                SettingsGroup {
                    SettingsRow(
                        label = "Appearance",
                        subtitle = "System",
                        iconBackground = Color(0xFF9FE1CB),
                        icon = { AppIcons.Theme24(tint = Color(0xFF04342C)) },
                        showDivider = false,
                        onClick = {},
                        trailing = { AppIcons.ChevronRight24(tint = AppTheme.colors.contentTertiary) },
                    )
                }
            }

            item {
                AppSectionLabel("Data")
                SettingsGroup {
                    SettingsRow(
                        label = "Reset data",
                        subtitle = "Erase all notes",
                        iconBackground = Color(0xFFF4C0D1),
                        icon = { AppIcons.Trash24(tint = Color(0xFF4B1528)) },
                        showDivider = false,
                        onClick = { onEvent(SettingsEvent.Ui.ResetDataClick) },
                        trailing = { AppIcons.ChevronRight24(tint = AppTheme.colors.contentTertiary) },
                    )
                }
            }

            item {
                AppSectionLabel("About")
                SettingsGroup {
                    SettingsRow(
                        label = "Version",
                        subtitle = "1.0 (build 1)",
                        iconBackground = Color(0xFFD3D1C7),
                        icon = {
                            Text(
                                text = "i",
                                style = AppTheme.typography.headlineSmall,
                                color = Color(0xFF2A2A26),
                            )
                        },
                    )
                    SettingsRow(
                        label = "Privacy",
                        iconBackground = Color(0xFFF5C4B3),
                        icon = { AppIcons.Lock24(tint = Color(0xFF3A1608)) },
                        showDivider = false,
                        onClick = {},
                        trailing = { AppIcons.ChevronRight24(tint = AppTheme.colors.contentTertiary) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsGroup(content: @Composable () -> Unit) {
    val shape = AppTheme.shapes.group
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(AppTheme.colors.bgCardPrimary)
            .glassBorder(shape = shape),
    ) {
        content()
    }
}

@Composable
private fun SettingsRow(
    label: String,
    iconBackground: Color,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showDivider: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailing: @Composable RowScope.() -> Unit = {},
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (onClick != null) Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = onClick,
                    ) else Modifier
                )
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(IconShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center,
            ) {
                icon()
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.contentPrimary,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.contentTertiary,
                    )
                }
            }
            trailing()
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .height(0.5.dp)
                    .background(AppTheme.colors.contentDividers),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSettings() {
    AppPreview {
        SettingsContent(
            state = SettingsState(),
            onEvent = {},
        )
    }
}
