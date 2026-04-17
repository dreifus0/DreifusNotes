package com.dreifus.template.uikit.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.button.TextButtonProperty.IconSize
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.utils.clickableWithAlphaIndication

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    appearance: TextButtonProperty.Appearance = TextButtonProperty.Appearance.Default,
    buttonColor: TextButtonProperty.ColorSet = TextButtonProperty.ColorSet.flat,
    startIcon: Painter? = null,
    startIconSize: IconSize = IconSize.Normal,
    status: ButtonStatus = ButtonStatus.Enabled,
    isEnabled: Boolean = status.isEnabled(),
    isLoading: Boolean = status.isLoading(),
    loaderAlignment: Alignment = Alignment.Center,
) {
    CompositionLocalProvider(
        //https://stackoverflow.com/questions/75392145/how-to-remove-surface-padding-in-jetpack-compose
        // по умолчанию минимальный размер кликабельного компонента 48dp,
        // устанавливаем, чтобы compose не добавлял доп паддинги к компонентам менее 48dp
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        val contentColor = rememberUpdatedState(
            if (isEnabled) {
                buttonColor.contentColor
            } else {
                buttonColor.contentColor.copy(alpha = 0.56f)
            }
        )
        Surface(
            modifier = Modifier
                .defaultMinSize(minHeight = appearance.minHeight)
                .clickableWithAlphaIndication(
                    onClick = { onClick() },
                    isEnabled = isEnabled
                )
                .then(modifier),
            color = buttonColor.buttonBackgroundColor,
            contentColor = contentColor.value,
        ) {
            Box(contentAlignment = loaderAlignment) {
                if (startIcon != null) {
                    FlatButtonContentTextWithIcon(
                        text = text,
                        startIcon = startIcon,
                        startIconSize = startIconSize,
                        isLoading = isLoading,
                        style = appearance.style(AppTheme.typography),
                    )
                } else {
                    FlatButtonContentText(
                        text = text,
                        isLoading = isLoading,
                        style = appearance.style(AppTheme.typography),
                    )
                }
                if (isLoading) {
                    FlatButtonContentLoading(
                        color = buttonColor.loaderColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun FlatButtonContentText(
    text: String,
    isLoading: Boolean,
    style: TextStyle,
) {
    Text(
        modifier = Modifier.alpha(if (isLoading) 0f else 1f),
        text = text,
        style = style,
    )
}

@Composable
private fun FlatButtonContentTextWithIcon(
    text: String,
    startIcon: Painter,
    startIconSize: IconSize,
    isLoading: Boolean,
    style: TextStyle,
) {
    Row(
        modifier = Modifier.alpha(if (isLoading) 0f else 1f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(end = startIconSize.paddingEnd)
                .let {
                    if (startIconSize is IconSize.WrapContent) {
                        it.wrapContentSize()
                    } else {
                        it.size(startIconSize.size)
                    }
                },
            painter = startIcon,
            contentDescription = null,
        )
        Text(
            text = text,
            style = style,
        )
    }
}

@Composable
private fun FlatButtonContentLoading(
    color: Color,
) {
    CircularProgressIndicator(color = color)
}

// region preview

@Preview
@Composable
private fun Preview() {
    AppPreview {
        Column {
            TextButton(
                modifier = Modifier.padding(4.dp),
                text = "Button",
                onClick = {},
            )

            TextButton(
                modifier = Modifier.padding(4.dp),
                text = "Button",
                onClick = {},
                isEnabled = false,
            )

            TextButton(
                modifier = Modifier.padding(4.dp),
                text = "Button",
                onClick = {},
                startIcon = ColorPainter(Color.Cyan),
            )

            TextButton(
                modifier = Modifier.padding(4.dp),
                text = "Button",
                onClick = {},
                startIcon = ColorPainter(Color.Cyan),
                isEnabled = false,
            )

            var isLoading by remember { mutableStateOf(false) }
            TextButton(
                modifier = Modifier.padding(4.dp),
                text = "Button",
                onClick = { isLoading = !isLoading },
                startIcon = ColorPainter(Color.Cyan),
                isLoading = isLoading,
            )

            TextButton(
                modifier = Modifier.padding(4.dp),
                text = "Button",
                onClick = {},
                startIcon = ColorPainter(Color.Cyan),
                isEnabled = false,
                isLoading = true,
            )
        }
    }
}

// endregion preview
