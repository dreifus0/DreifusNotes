package com.dreifus.template.uikit.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.rememberHazeState

private val TabsShape = RoundedCornerShape(20.dp)

@Composable
fun <T> TabBar(
    modifier: Modifier = Modifier,
    tabs: List<TabInfo<T>>,
    hazeState: HazeState,
    currentTab: TabInfo<T>? = null,
    onTabClick: (TabInfo<T>) -> Unit,
) {
    val bgBase = AppTheme.colors.backgroundBase
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        bgBase.copy(alpha = 0.85f),
                        bgBase,
                    )
                )
            )
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 63.dp)
                .weight(1f)
                .clip(TabsShape)
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        tint = HazeTint(
                            if (AppTheme.colors.isDarkTheme) {
                                bgBase.copy(alpha = 0.85f)
                            } else {
                                bgBase.copy(alpha = 0.5f)
                            }
                        ),
                        blurRadius = 20.dp,
                    ),
                )
                .background(color = AppTheme.colors.backgroundSecondary.copy(alpha = 0.4f))
                .border(0.5.dp, AppTheme.colors.contentBorder, TabsShape)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEach { tab ->
                val isSelected = currentTab?.data == tab.data
                TabItem(
                    tab = tab,
                    isSelected = isSelected,
                    onClick = { onTabClick(tab) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun <T> TabItem(
    tab: TabInfo<T>,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .then(
                if (isSelected) {
                    Modifier.background(
                        color = AppTheme.colors.backgroundActive,
                        shape = RoundedCornerShape(32.dp),
                    )
                } else {
                    Modifier
                }
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(2.dp)
        Icon(
            painter = tab.icon(),
            contentDescription = null,
            tint = if (isSelected) AppTheme.colors.accentSecondary
            else AppTheme.colors.contentSecondary,
        )
        Spacer(2.dp)
        Text(
            text = tab.title(),
            style = AppTheme.typography.headlineSmall,
            color = if (isSelected) AppTheme.colors.accentSecondary
            else AppTheme.colors.contentSecondary,
        )
        Spacer(6.dp)
    }
}

data class TabInfo<T>(
    val icon: @Composable () -> Painter,
    val title: @Composable () -> String,
    val data: T,
)

//private val previewTabs = listOf(
//    TabInfo(iconRes = R.drawПодable.ic_morning, titleRes = R.string.preview_tab_home, data = 0),
//    TabInfo(iconRes = R.drawable.ic_day, titleRes = R.string.preview_tab_mind, data = 1),
//)
//
//@Preview(showBackground = true)
//@Composable
//private fun PreviewFirstTabSelected() {
//    AppPreview {
//        TabBar(
//            tabs = previewTabs,
//            hazeState = rememberHazeState(),
//            currentTab = previewTabs[0],
//            onTabClick = {},
//        )
//    }
//}
