package com.dreifus.template.uikit.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.style.AppIcon
import com.dreifus.template.uikit.style.AppTheme

private val TabShape = RoundedCornerShape(32.dp)

@Composable
fun <T> TabBar(
    modifier: Modifier = Modifier,
    tabs: List<TabInfo<T>>,
    currentTab: TabInfo<T>? = null,
    onTabClick: (TabInfo<T>) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundBase)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(TabShape)
                .background(AppTheme.colors.bgCardPrimary)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEach { tab ->
                TabItem(
                    tab = tab,
                    isSelected = currentTab?.data == tab.data,
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
    val activeColor = AppTheme.colors.accentSecondary
    val inactiveColor = AppTheme.colors.contentSecondary
    val tint = if (isSelected) activeColor else inactiveColor
    Column(
        modifier = modifier
            .clip(TabShape)
            .run {
                if (isSelected) this.background(AppTheme.colors.backgroundBase)
                else this
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { onClick() }
            .padding(vertical = 5.dp, horizontal = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        tab.icon(tint = tint)
        Text(
            text = tab.title(),
            style = AppTheme.typography.headlineMedium,
            color = tint,
        )
    }
}

data class TabInfo<T>(
    val icon: AppIcon,
    val title: @Composable () -> String,
    val data: T,
)
