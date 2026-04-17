package com.dreifus.template.uikit.row

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.icon.ChevronRight24
import com.dreifus.template.uikit.icon.Clock24
import com.dreifus.template.uikit.icon.Fingerprint24
import com.dreifus.template.uikit.icon.Lock24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.toggle.Toggle

@Composable
fun AppSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        style = AppTheme.typography.bodySmall,
        color = AppTheme.colors.contentTertiary,
        modifier = modifier.padding(horizontal = 6.dp, vertical = 5.dp),
    )
}

@Composable
fun AppGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.group)
            .background(AppTheme.colors.backgroundSecondary),
    ) {
        content()
    }
}

@Composable
fun AppRow(
    label: String,
    modifier: Modifier = Modifier,
    iconBackground: Color = Color.Transparent,
    icon: @Composable (() -> Unit)? = null,
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
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(AppTheme.shapes.rowIcon)
                        .background(iconBackground),
                    contentAlignment = Alignment.Center,
                ) {
                    icon()
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.contentPrimary,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = AppTheme.typography.bodySmall.copy(
                            fontSize = AppTheme.typography.bodySmall.fontSize * 0.85f,
                        ),
                        color = AppTheme.colors.contentSecondary,
                    )
                }
            }

            trailing()
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(0.5.dp)
                    .background(AppTheme.colors.contentDividers),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewApp() {
    AppPreview {
        Column(modifier = Modifier.padding(11.dp)) {
            AppSectionLabel("Security")
            AppGroup {
                var pinEnabled by remember { mutableStateOf(true) }
                var bioEnabled by remember { mutableStateOf(true) }

                AppRow(
                    label = "App PIN",
                    iconBackground = Color(0xFFEEEDFE),
                    icon = { AppIcons.Lock24(tint = AppTheme.colors.accentPrimary) },
                    trailing = {
                        Toggle(
                            checked = pinEnabled,
                            onCheckedChange = { pinEnabled = it })
                    },
                )
                AppRow(
                    label = "Biometrics",
                    iconBackground = Color(0xFFEEEDFE),
                    icon = { AppIcons.Fingerprint24(tint = AppTheme.colors.accentPrimary) },
                    trailing = {
                        Toggle(
                            checked = bioEnabled,
                            onCheckedChange = { bioEnabled = it })
                    },
                )
                AppRow(
                    label = "Auto-lock",
                    iconBackground = Color(0xFFEEEDFE),
                    icon = { AppIcons.Clock24(tint = AppTheme.colors.accentPrimary) },
                    showDivider = false,
                    onClick = {},
                    trailing = {
                        Text(
                            "1 min",
                            style = AppTheme.typography.bodySmall,
                            color = AppTheme.colors.contentTertiary
                        )
                        AppIcons.ChevronRight24()
                    },
                )
            }
        }
    }
}
