package com.dreifus.template.uikit.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.style.AppTheme

@Composable
fun <T> TabBar(
    modifier: Modifier,
    tabs: List<TabInfo<T>>,
    currentTab: TabInfo<T>? = null,
    onTabClick: (TabInfo<T>) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
    ) {
        tabs.forEach { tab ->
            val isSelected = currentTab?.data == tab.data
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (!isSelected) {
                            onTabClick(tab)
                        }
                    }
                    .let { tab.modifier(it) }
                    .padding(top = 16.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = tab.icon(),
                    contentDescription = null,
                    tint = if (isSelected) AppTheme.colors.accentSecondary else AppTheme.colors.contentSecondary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tab.title(),
                    style = AppTheme.typography.headlineMedium,
                    color = if (isSelected) AppTheme.colors.accentSecondary else AppTheme.colors.contentSecondary,
                )
            }
        }
    }
}

data class TabInfo<T>(
    val icon: @Composable () -> Painter,
    val title: @Composable () -> String,
    val modifier: @Composable Modifier.() -> Modifier = { this },
    val data: T,
)
