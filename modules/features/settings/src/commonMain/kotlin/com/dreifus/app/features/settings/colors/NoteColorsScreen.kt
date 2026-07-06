package com.dreifus.app.features.settings.colors

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.settings.colors.mvu.MAX_FAVORITE_COLORS
import com.dreifus.app.features.settings.colors.mvu.NoteColorsEffect
import com.dreifus.app.features.settings.colors.mvu.NoteColorsEvent
import com.dreifus.app.features.settings.colors.mvu.NoteColorsState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.icon.Plus24
import com.dreifus.template.uikit.icon.Trash24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.row.AppSectionLabel
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.NoteCardColor
import dev.zacsweers.metrox.viewmodel.metroViewModel
import dreifusnotes.modules.features.settings.generated.resources.Res
import dreifusnotes.modules.features.settings.generated.resources.note_colors_footer
import dreifusnotes.modules.features.settings.generated.resources.note_colors_picker_add
import dreifusnotes.modules.features.settings.generated.resources.note_colors_picker_cancel
import dreifusnotes.modules.features.settings.generated.resources.note_colors_picker_title
import dreifusnotes.modules.features.settings.generated.resources.note_colors_section
import dreifusnotes.modules.features.settings.generated.resources.note_colors_selected_count
import dreifusnotes.modules.features.settings.generated.resources.note_colors_title
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

private const val SWATCHES_PER_ROW = 5

class NoteColorsScreen : RegularScreen {

    @Composable
    override fun Content() {
        val vm = metroViewModel<NoteColorsViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val regularNav = Navigation.regular

        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    NoteColorsEffect.NavigateBack -> regularNav.pop()
                }
            }
        }

        NoteColorsContent(state = state, onEvent = vm::dispatch)
    }
}

@Composable
private fun NoteColorsContent(
    state: NoteColorsState,
    onEvent: (NoteColorsEvent.Ui) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.backgroundSecondary)
                    .clickable { onEvent(NoteColorsEvent.Ui.BackClick) },
                contentAlignment = Alignment.Center,
            ) {
                AppIcons.ArrowLeft24(tint = AppTheme.colors.contentPrimary)
            }
            Text(
                text = stringResource(Res.string.note_colors_title),
                style = AppTheme.typography.heading5,
                color = AppTheme.colors.contentPrimary,
            )
        }

        Column(modifier = Modifier.padding(horizontal = 22.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    AppSectionLabel(stringResource(Res.string.note_colors_section))
                }
                Text(
                    text = stringResource(
                        Res.string.note_colors_selected_count,
                        state.favorites.size,
                        MAX_FAVORITE_COLORS,
                    ),
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.contentTertiary,
                )
                if (state.canDelete) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { onEvent(NoteColorsEvent.Ui.DeleteClick) },
                        contentAlignment = Alignment.Center,
                    ) {
                        AppIcons.Trash24(
                            modifier = Modifier.size(18.dp),
                            tint = AppTheme.colors.accentError,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppTheme.shapes.group)
                    .background(AppTheme.colors.backgroundSecondary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // Favorite swatches plus a trailing "+" tile while there is room for more.
                val cellCount = state.favorites.size + if (state.canAdd) 1 else 0
                (0 until cellCount).chunked(SWATCHES_PER_ROW).forEach { rowIndices ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        rowIndices.forEach { index ->
                            val color = state.favorites.getOrNull(index)
                            if (color != null) {
                                ColorSwatch(
                                    color = color,
                                    isHighlighted = color == state.highlighted,
                                    onClick = { onEvent(NoteColorsEvent.Ui.ColorTap(color)) },
                                )
                            } else {
                                AddColorTile(onClick = { showPicker = true })
                            }
                        }
                    }
                }
            }
            Text(
                text = stringResource(Res.string.note_colors_footer),
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.contentTertiary,
                modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 14.dp),
            )
        }
    }

    if (showPicker) {
        ColorPickerDialog(
            onConfirm = { color ->
                showPicker = false
                onEvent(NoteColorsEvent.Ui.AddColor(NoteCardColor(color)))
            },
            onDismiss = { showPicker = false },
        )
    }
}

@Composable
private fun ColorSwatch(
    color: NoteCardColor,
    isHighlighted: Boolean,
    onClick: () -> Unit,
) {
    // Show the raw seed, not the theme-adapted palette background, so the swatch matches
    // what was picked in the color picker regardless of light/dark theme.
    Box(
        modifier = Modifier
            .size(44.dp)
            .then(
                if (isHighlighted) Modifier.border(2.dp, AppTheme.colors.accentPrimary, CircleShape)
                else Modifier
            )
            .padding(if (isHighlighted) 5.dp else 0.dp)
            .clip(CircleShape)
            .background(color.seed)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (isHighlighted) {
            val checkColor =
                if (color.seed.luminance() > 0.5f) Color.Black.copy(alpha = 0.75f) else Color.White
            Canvas(modifier = Modifier.size(14.dp)) {
                val w = size.width
                val h = size.height
                val stroke = w * 0.16f
                drawLine(
                    color = checkColor,
                    start = Offset(w * 0.18f, h * 0.52f),
                    end = Offset(w * 0.42f, h * 0.76f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = checkColor,
                    start = Offset(w * 0.42f, h * 0.76f),
                    end = Offset(w * 0.82f, h * 0.26f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

@Composable
private fun AddColorTile(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .border(1.5.dp, AppTheme.colors.contentBorder, CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        AppIcons.Plus24(
            modifier = Modifier.size(18.dp),
            tint = AppTheme.colors.contentSecondary,
        )
    }
}

@Composable
private fun ColorPickerDialog(
    onConfirm: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    var hue by remember { mutableStateOf(260f) }
    var saturation by remember { mutableStateOf(0.65f) }
    var lightness by remember { mutableStateOf(0.80f) }
    val color = Color.hsl(hue, saturation, lightness)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.note_colors_picker_title),
                style = AppTheme.typography.headlineLarge,
                color = AppTheme.colors.contentPrimary,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(1.dp, AppTheme.colors.contentBorder, CircleShape),
                )
                GradientSlider(
                    value = hue / 360f,
                    onValueChange = { hue = it * 360f },
                    brushColors = List(13) { Color.hsl(it * 30f % 360f, 1f, 0.5f) },
                )
                GradientSlider(
                    value = saturation,
                    onValueChange = { saturation = it },
                    brushColors = listOf(
                        Color.hsl(hue, 0f, lightness),
                        Color.hsl(hue, 1f, lightness),
                    ),
                )
                GradientSlider(
                    value = lightness,
                    onValueChange = { lightness = it },
                    brushColors = listOf(
                        Color.Black,
                        Color.hsl(hue, saturation, 0.5f),
                        Color.White,
                    ),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(color) }) {
                Text(
                    text = stringResource(Res.string.note_colors_picker_add),
                    color = AppTheme.colors.accentPrimary,
                    style = AppTheme.typography.headlineMedium,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(Res.string.note_colors_picker_cancel),
                    color = AppTheme.colors.contentSecondary,
                    style = AppTheme.typography.headlineMedium,
                )
            }
        },
        containerColor = AppTheme.colors.backgroundBase,
        shape = AppTheme.shapes.dialog,
    )
}

@Composable
private fun GradientSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    brushColors: List<Color>,
) {
    val thumbSize = 24.dp
    var trackWidth by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .onGloballyPositioned { trackWidth = it.size.width }
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.horizontalGradient(brushColors))
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onValueChange((offset.x / size.width).coerceIn(0f, 1f))
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    onValueChange((change.position.x / size.width).coerceIn(0f, 1f))
                }
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        val thumbPx = with(LocalDensity.current) { thumbSize.toPx() }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (value * (trackWidth - thumbPx)).roundToInt().coerceAtLeast(0),
                        y = 0,
                    )
                }
                .padding(2.dp)
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.9f))
                .border(1.dp, Color(0x33000000), CircleShape),
        )
    }
}

@Preview
@Composable
private fun PreviewNoteColors() {
    AppPreview {
        NoteColorsContent(
            state = NoteColorsState(),
            onEvent = {},
        )
    }
}
