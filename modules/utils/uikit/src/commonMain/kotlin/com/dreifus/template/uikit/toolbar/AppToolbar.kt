package com.dreifus.template.uikit.toolbar

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.icon.Edit24
import com.dreifus.template.uikit.icon.Lock24
import com.dreifus.template.uikit.icon.MoreHoriz24
import com.dreifus.template.uikit.icon.Trash24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcon
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme

private val emptyAction = {}

@Composable
fun AppToolbar(
    title: String,
    button: ToolbarButton?,
    modifier: Modifier = Modifier,
    titleVisible: Boolean = true,
    centerTitle: Boolean = true,
) {
    AppToolbar(
        title = title,
        modifier = modifier,
        titleVisible = titleVisible,
        startIcon = if (button?.position == ToolbarPosition.START) button.icon.painter else null,
        startIconClick = if (button?.position == ToolbarPosition.START) button.onClick() else emptyAction,
        endIcon = if (button?.position == ToolbarPosition.END) button.icon.painter else null,
        endIconClick = if (button?.position == ToolbarPosition.END) button.onClick() else emptyAction,
        centerTitle = centerTitle,
    )
}

@Suppress("CyclomaticComplexMethod")
@Composable
fun AppToolbar(
    title: String,
    buttons: List<ToolbarButton>,
    modifier: Modifier = Modifier,
    titleVisible: Boolean = true,
    centerTitle: Boolean = true,
) {
    @Composable
    fun List<ToolbarButton>.asBlock() {
        forEachIndexed { index, button ->
            val onClick = button.onClick()
            if (index > 0) {
                Spacer(Modifier.width(8.dp))
            }
            button.icon(modifier = buttonModifier(onClick), tint = button.tint)
        }
    }

    @Composable
    fun buttonsBlock(position: ToolbarPosition): (@Composable RowScope.() -> Unit)? =
        buttons.filter { it.position == position }
            .takeIf { it.isNotEmpty() }
            ?.let { { it.asBlock() } }

    AppToolbar(
        title = title,
        modifier = modifier,
        titleVisible = titleVisible,
        startBlock = buttonsBlock(ToolbarPosition.START),
        endBlock = buttonsBlock(ToolbarPosition.END),
        centerTitle = centerTitle,
    )
}

@Suppress("CyclomaticComplexMethod")
@Composable
fun AppToolbar(
    title: String,
    modifier: Modifier = Modifier,
    startIcon: Painter? = null,
    startIconClick: () -> Unit = emptyAction,
    endIcon: Painter? = null,
    endIconClick: () -> Unit = emptyAction,
    titleVisible: Boolean = true,
    centerTitle: Boolean = true,
) {
    AppToolbar(
        title = title,
        modifier = modifier,
        startBlock = startIcon?.let {
            {
                Image(
                    painter = it,
                    contentDescription = null,
                    modifier = buttonModifier { startIconClick() },
                )
            }
        },
        titleVisible = titleVisible,
        endBlock = endIcon?.let {
            {
                Icon(
                    painter = it,
                    contentDescription = null,
                    modifier = buttonModifier { endIconClick() },
                )
            }
        },
        centerTitle = centerTitle,
    )
}

@Suppress("CyclomaticComplexMethod")
@Composable
fun AppToolbar(
    title: String,
    modifier: Modifier = Modifier,
    titleVisible: Boolean = true,
    centerTitle: Boolean = true,
    startBlock: (@Composable RowScope.() -> Unit)? = null,
    endBlock: (@Composable RowScope.() -> Unit)?,
) {
    val titleAlpha = remember { Animatable(if (titleVisible) 1f else 0f) }
    LaunchedEffect(titleVisible) {
        titleAlpha.animateTo(targetValue = if (titleVisible) 1f else 0f)
    }
    val showStartIcon = startBlock != null
    val showEndIcon = endBlock != null
    val padding = if (!centerTitle && (showStartIcon || showEndIcon)) {
        PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    } else when {
        showStartIcon && showEndIcon -> PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
        showStartIcon -> PaddingValues(start = 4.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
        showEndIcon -> PaddingValues(start = 16.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
        else -> PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
    }
    CompositionLocalProvider(
        LocalContentColor provides AppTheme.colors.contentPrimary,
    ) {
        Row(
            modifier = Modifier
                .then(modifier)
                .let { if (centerTitle) it.height(44.dp) else it }
                .fillMaxWidth()
                .padding(padding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            startBlock?.invoke(this)
            val textAlign = if (!centerTitle) TextAlign.Start else when {
                showStartIcon -> TextAlign.Center
                showEndIcon -> TextAlign.Start
                else -> TextAlign.Center
            }
            val textPadding = if (!centerTitle) PaddingValues() else when {
                showStartIcon && !showEndIcon -> PaddingValues(end = 28.dp)
                else -> PaddingValues()
            }
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = if (!centerTitle) 16.dp else 0.dp)
                    .padding(textPadding)
                    .alpha(titleAlpha.value),
                style = AppTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = textAlign,
            )
            endBlock?.invoke(this)
        }
    }
}

private fun buttonModifier(onClick: () -> Unit) = Modifier
    .clip(CircleShape)
    .clickable(onClick = onClick)
    .padding(8.dp)

interface ToolbarButton {
    val position: ToolbarPosition
    val icon: AppIcon
    val tint: Color get() = Color.Unspecified

    @Composable
    fun onClick(): () -> Unit

    companion object {
        fun stub(position: ToolbarPosition, icon: AppIcon) = object : ToolbarButton {
            override val position = position
            override val icon = icon

            @Composable
            override fun onClick(): () -> Unit = {}
        }
    }
}

data class EndToolbarButton(
    override val icon: AppIcon,
    override val tint: Color = Color.Unspecified,
    val onClick: () -> Unit,
) : ToolbarButton {
    override val position: ToolbarPosition = ToolbarPosition.END

    @Composable
    override fun onClick() = onClick
}

data class StartToolbarButton(
    override val icon: AppIcon,
    override val tint: Color = Color.Unspecified,
    val onClick: () -> Unit,
) : ToolbarButton {
    override val position: ToolbarPosition = ToolbarPosition.START

    @Composable
    override fun onClick() = onClick
}

enum class ToolbarPosition {
    START,
    END,
}

@Preview
@Composable
private fun PreviewToolbarTitleOnly() {
    AppPreview {
        AppToolbar(title = "Notes", button = null)
    }
}

@Preview
@Composable
private fun PreviewToolbarWithBackButton() {
    AppPreview {
        AppToolbar(
            title = "My note",
            button = ToolbarButton.stub(ToolbarPosition.START, AppIcons.ArrowLeft24),
        )
    }
}

@Preview
@Composable
private fun PreviewToolbarWithEndButton() {
    AppPreview {
        AppToolbar(
            title = "My note",
            button = ToolbarButton.stub(ToolbarPosition.END, AppIcons.Edit24),
        )
    }
}

@Preview
@Composable
private fun PreviewToolbarWithBothButtons() {
    AppPreview {
        AppToolbar(
            title = "My note",
            buttons = listOf(
                ToolbarButton.stub(ToolbarPosition.START, AppIcons.ArrowLeft24),
                ToolbarButton.stub(ToolbarPosition.END, AppIcons.Edit24),
                ToolbarButton.stub(ToolbarPosition.END, AppIcons.Trash24),
            ),
        )
    }
}

@Preview
@Composable
private fun PreviewToolbarLeftAligned() {
    AppPreview {
        AppToolbar(
            title = "Project ideas",
            centerTitle = false,
            buttons = listOf(
                StartToolbarButton(AppIcons.ArrowLeft24) {},
                EndToolbarButton(AppIcons.Lock24) {},
                EndToolbarButton(AppIcons.MoreHoriz24) {},
            ),
        )
    }
}
