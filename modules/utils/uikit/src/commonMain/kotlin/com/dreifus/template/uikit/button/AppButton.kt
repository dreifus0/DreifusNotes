package com.dreifus.template.uikit.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.button.AppButtonProperty.IconSize
import com.dreifus.template.uikit.glass.glassBorder
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.utils.doubleClickDefence

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: AppButtonProperty.ButtonSize = AppButtonProperty.ButtonSize.Medium,
    appearance: AppButtonProperty.Appearance = AppButtonProperty.Appearance.primary,
    icon: Painter? = null,
    iconSize: IconSize = IconSize.WrapContent,
    isStartIcon: Boolean = true,
    status: ButtonStatus = ButtonStatus.Enabled,
) {
    val isEnabled: Boolean = status.isEnabled()
    val isLoading: Boolean = status.isLoading()
    val disabledContainerAlpha = if (status == ButtonStatus.Disabled) {
        0.56f
    } else { // чтобы контейнер не становился более прозрачным для Loading статуса
        1f
    }
    val buttonShape = RoundedCornerShape(48.dp)
    Button(
        onClick = { doubleClickDefence(onClick) },
        modifier = Modifier
            .defaultMinSize(minHeight = buttonSize.height)
            .testTag(text)
            .let { mod ->
                val brush = appearance.brush
                if (brush != null) {
                    mod
                        .alpha(disabledContainerAlpha)
                        .clip(buttonShape)
                        .background(brush)
                } else mod
            }
            .then(if (appearance.shouldDrawGlassBorder) modifier.glassBorder() else modifier),
        enabled = isEnabled,
        shape = buttonShape,
        elevation = null,
        contentPadding = PaddingValues(0.dp),
        colors = when {
            appearance.brush != null -> ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = appearance.contentColor,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = appearance.contentColor,
            )

            appearance.drawStyle == Fill -> ButtonDefaults.buttonColors(
                containerColor = appearance.buttonBackgroundColor,
                contentColor = appearance.contentColor,
                disabledContainerColor = appearance.buttonBackgroundColor.copy(alpha = disabledContainerAlpha),
                disabledContentColor = appearance.contentColor.copy(alpha = disabledContainerAlpha),
            )

            else -> ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = appearance.contentColor,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = appearance.contentColor.copy(alpha = disabledContainerAlpha),
            )
        },
        border = when (appearance.drawStyle) {
            Fill -> null
            is Stroke -> BorderStroke(
                2.dp,
                appearance.buttonBackgroundColor.copy(alpha = disabledContainerAlpha)
            )
        }
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (icon != null) {
                ButtonContentTextWithIcon(
                    text = text,
                    buttonSize = buttonSize,
                    icon = icon,
                    iconSize = iconSize,
                    isStartIcon = isStartIcon,
                    isLoading = isLoading,
                )
            } else {
                ButtonContentText(
                    text = text,
                    buttonSize = buttonSize,
                    isLoading = isLoading,
                )
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = appearance.contentColor,
                    strokeWidth = 3.dp,
                )
            }
        }
    }
}

@Composable
private fun ButtonContentText(
    text: String,
    buttonSize: AppButtonProperty.ButtonSize,
    isLoading: Boolean,
) {
    Text(
        modifier = Modifier
            .alpha(if (isLoading) 0f else 1f)
            .padding(
                horizontal = buttonSize.horizontalPadding,
                vertical = buttonSize.verticalPadding,
            ),
        text = text,
        textAlign = TextAlign.Center,
        style = AppTheme.typography.headlineLarge,
        color = AppTheme.colors.accentOnPrimary,
    )
}

@Composable
private fun ButtonContentTextWithIcon(
    text: String,
    buttonSize: AppButtonProperty.ButtonSize,
    icon: Painter,
    iconSize: IconSize,
    isStartIcon: Boolean,
    isLoading: Boolean,
) {
    Row(
        modifier = Modifier
            .alpha(if (isLoading) 0f else 1f)
            .padding(
                horizontal = buttonSize.horizontalPadding,
                vertical = buttonSize.verticalPadding,
            )
            .defaultMinSize(minHeight = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isStartIcon) {
            IconInButton(
                iconSize = iconSize,
                icon = icon,
                endPadding = if (text.isNotEmpty()) {
                    iconSize.padding
                } else {
                    0.dp
                },
            )
        }
        if (text.isNotEmpty()) {
            Text(
                text = text,
                style = AppTheme.typography.headlineLarge,
                color = AppTheme.colors.accentOnPrimary,
            )
        }
        if (!isStartIcon) {
            IconInButton(
                iconSize = iconSize,
                icon = icon,
                startPadding = if (text.isNotEmpty()) {
                    iconSize.padding
                } else {
                    0.dp
                },
            )
        }
    }
}

@Composable
private fun IconInButton(
    iconSize: IconSize,
    icon: Painter,
    startPadding: Dp = 0.dp,
    endPadding: Dp = 0.dp,
) {
    Icon(
        modifier = Modifier
            .padding(
                start = startPadding,
                end = endPadding,
            )
            .let {
                if (iconSize is IconSize.WrapContent) {
                    it.wrapContentSize()
                } else {
                    it.size(iconSize.size)
                }
            },
        painter = icon,
        contentDescription = null,
        tint = Color.Unspecified, // По-дефолту применяется тинт
    )
}

@Preview(heightDp = 1000)
@Composable
private fun Preview() {
    AppPreview {
        Column(modifier = Modifier.fillMaxHeight()) {
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                AppButton(
                    text = "Button",
                    onClick = {},
                    appearance = AppButtonProperty.Appearance.primary,
                )
                Spacer(Modifier.width(12.dp))
                AppButton(
                    text = "Button",
                    onClick = {},
                    appearance = AppButtonProperty.Appearance.primary,
                    status = ButtonStatus.Disabled,
                )
            }

            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                AppButton(
                    text = "Button",
                    onClick = {},
                )
                Spacer(Modifier.width(12.dp))
                AppButton(
                    text = "Button",
                    onClick = {},
                    status = ButtonStatus.Disabled,
                )
            }

            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                AppButton(
                    text = "Button",
                    onClick = {},
                    icon = ColorPainter(Color.Cyan),
                )
                Spacer(Modifier.width(12.dp))
                AppButton(
                    text = "Button",
                    onClick = {},
                    icon = ColorPainter(Color.Cyan),
                    status = ButtonStatus.Disabled,
                )
            }

        }
    }
}
