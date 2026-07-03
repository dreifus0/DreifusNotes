package com.dreifus.app.features.settings.appearance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.data.preferences.ThemeMode
import com.dreifus.app.features.settings.appearance.mvu.AppearanceEffect
import com.dreifus.app.features.settings.appearance.mvu.AppearanceEvent
import com.dreifus.app.features.settings.appearance.mvu.AppearanceState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.row.AppSectionLabel
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import dev.zacsweers.metrox.viewmodel.metroViewModel
import dreifusnotes.modules.features.settings.generated.resources.Res
import dreifusnotes.modules.features.settings.generated.resources.appearance_dark
import dreifusnotes.modules.features.settings.generated.resources.appearance_dark_sub
import dreifusnotes.modules.features.settings.generated.resources.appearance_footer
import dreifusnotes.modules.features.settings.generated.resources.appearance_light
import dreifusnotes.modules.features.settings.generated.resources.appearance_light_sub
import dreifusnotes.modules.features.settings.generated.resources.appearance_section_theme
import dreifusnotes.modules.features.settings.generated.resources.appearance_system
import dreifusnotes.modules.features.settings.generated.resources.appearance_system_sub
import dreifusnotes.modules.features.settings.generated.resources.appearance_title
import org.jetbrains.compose.resources.stringResource

class AppearanceScreen : RegularScreen {

    @Composable
    override fun Content() {
        val vm = metroViewModel<AppearanceViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val regularNav = Navigation.regular

        LaunchedEffect(Unit) {
            vm.dispatch(AppearanceEvent.Ui.Init)
        }
        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    AppearanceEffect.NavigateBack -> regularNav.pop()
                }
            }
        }

        AppearanceContent(state = state, onEvent = vm::dispatch)
    }
}

private data class ThemeOption(
    val mode: ThemeMode,
    val label: String,
    val subtitle: String,
)

@Composable
private fun AppearanceContent(
    state: AppearanceState,
    onEvent: (AppearanceEvent.Ui) -> Unit,
) {
    val options = listOf(
        ThemeOption(ThemeMode.System, stringResource(Res.string.appearance_system), stringResource(Res.string.appearance_system_sub)),
        ThemeOption(ThemeMode.Light, stringResource(Res.string.appearance_light), stringResource(Res.string.appearance_light_sub)),
        ThemeOption(ThemeMode.Dark, stringResource(Res.string.appearance_dark), stringResource(Res.string.appearance_dark_sub)),
    )

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
                    .clickable { onEvent(AppearanceEvent.Ui.BackClick) },
                contentAlignment = Alignment.Center,
            ) {
                AppIcons.ArrowLeft24(tint = AppTheme.colors.contentPrimary)
            }
            Text(
                text = stringResource(Res.string.appearance_title),
                style = AppTheme.typography.heading5,
                color = AppTheme.colors.contentPrimary,
            )
        }

        Column(modifier = Modifier.padding(horizontal = 22.dp)) {
            AppSectionLabel(stringResource(Res.string.appearance_section_theme))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppTheme.shapes.group)
                    .background(AppTheme.colors.backgroundSecondary),
            ) {
                options.forEachIndexed { index, option ->
                    ThemeOptionRow(
                        option = option,
                        selected = option.mode == state.selected,
                        onClick = { onEvent(AppearanceEvent.Ui.Select(option.mode)) },
                    )
                    if (index < options.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(0.5.dp)
                                .background(AppTheme.colors.contentDividers),
                        )
                    }
                }
            }
            Text(
                text = stringResource(Res.string.appearance_footer),
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.contentTertiary,
                modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 14.dp),
            )
        }
    }
}

@Composable
private fun ThemeOptionRow(
    option: ThemeOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        ThemePreviewTile(option.mode)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.label,
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.contentPrimary,
            )
            Text(
                text = option.subtitle,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.contentTertiary,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        ThemeRadio(selected = selected)
    }
}

@Composable
private fun ThemeRadio(selected: Boolean) {
    val checkColor = AppTheme.colors.accentOnPrimary
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .then(
                if (selected) {
                    Modifier.background(AppTheme.colors.accentPrimary)
                } else {
                    Modifier.border(1.5.dp, AppTheme.colors.contentBorder, CircleShape)
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Canvas(modifier = Modifier.size(11.dp)) {
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

/**
 * Small illustrative swatch of what each theme looks like. Colors are intentionally literal
 * (independent of the active app theme) so the tiles read as previews.
 */
@Composable
private fun ThemePreviewTile(mode: ThemeMode) {
    val lightBg = Color(0xFFFFFFFF)
    val darkBg = Color(0xFF161421)
    val violetLight = Color(0xFFCECBF6)
    val roseLight = Color(0xFFF4C0D1)
    val violetDark = Color(0xFF3A3461)
    val roseDark = Color(0xFF4F2C3D)
    val lineLight = Color(0xFF9DA1AB)
    val lineDark = Color(0xFF5B5466)

    val card1: Color
    val card2: Color
    val line: Color
    when (mode) {
        ThemeMode.Light -> {
            card1 = violetLight; card2 = roseLight; line = lineLight
        }
        ThemeMode.Dark -> {
            card1 = violetDark; card2 = roseDark; line = lineDark
        }
        ThemeMode.System -> {
            card1 = violetLight; card2 = roseDark; line = lineLight
        }
    }

    Box(
        modifier = Modifier
            .size(width = 56.dp, height = 72.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, AppTheme.colors.contentDividers, RoundedCornerShape(10.dp)),
    ) {
        // Background — split for System, solid otherwise
        if (mode == ThemeMode.System) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(lightBg))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(darkBg))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().background(if (mode == ThemeMode.Dark) darkBg else lightBg))
        }
        // Mini content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 6.dp, end = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(3.dp)).background(card1))
            Box(modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(3.dp)).background(card2))
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(3.dp).clip(RoundedCornerShape(2.dp)).background(line))
            Box(modifier = Modifier.fillMaxWidth(0.5f).height(3.dp).clip(RoundedCornerShape(2.dp)).background(line))
        }
    }
}

@Preview
@Composable
private fun PreviewAppearance() {
    AppPreview {
        AppearanceContent(
            state = AppearanceState(selected = ThemeMode.System),
            onEvent = {},
        )
    }
}
