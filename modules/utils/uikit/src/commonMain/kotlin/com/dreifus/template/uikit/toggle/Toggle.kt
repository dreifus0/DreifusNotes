package com.dreifus.template.uikit.toggle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppTheme

@Composable
fun Toggle(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = AppTheme.colors
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.accentOnPrimary,
            checkedTrackColor = colors.accentPrimary,
            checkedBorderColor = colors.accentPrimary,
            uncheckedThumbColor = colors.backgroundBase,
            uncheckedTrackColor = colors.contentTertiary,
            uncheckedBorderColor = colors.contentTertiary,
            disabledCheckedThumbColor = colors.backgroundBase,
            disabledCheckedTrackColor = colors.backgroundDisabled,
            disabledCheckedBorderColor = colors.backgroundDisabled,
            disabledUncheckedThumbColor = colors.backgroundBase,
            disabledUncheckedTrackColor = colors.backgroundDisabled,
            disabledUncheckedBorderColor = colors.backgroundDisabled,
        ),
    )
}

@Preview
@Composable
private fun PreviewToggle() {
    AppPreview {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            var a by remember { mutableStateOf(true) }
            var b by remember { mutableStateOf(false) }
            Toggle(checked = a, onCheckedChange = { a = it })
            Toggle(checked = b, onCheckedChange = { b = it })
            Toggle(checked = true, onCheckedChange = null, enabled = false)
        }
    }
}
