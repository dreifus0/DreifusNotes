package com.dreifus.template.uikit.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.style.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TabRow(
    selectedTabIndex: Int,
    tabs: @Composable @UiComposable () -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = AppTheme.colors.backgroundBase,
        contentColor = AppTheme.colors.accentLink,
        tabs = tabs,
    )
}

@Composable
fun OldTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Tab(
        selected = selected,
        onClick = { onClick() },
        text = {
            Text(
                text = text,
                color = if (selected) {
                    AppTheme.colors.accentLink
                } else {
                    AppTheme.colors.contentPrimary
                },
                style = AppTheme.typography.bodyMedium,
            )
        }
    )
}

@Composable
fun Tab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    counter: Int? = null,
) {
    Tab(
        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
        selected = selected,
        onClick = { onClick() },
        selectedContentColor = AppTheme.colors.accentSecondary,
        unselectedContentColor = AppTheme.colors.contentPrimary,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = AppTheme.typography.headlineLarge,
            )
            if (counter != null) {
                Text(
                    text = counter.toString(),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.backgroundSecondary)
                        .padding(horizontal = 6.dp),
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.contentPrimary,
                )
            }
        }
    }
}

@Composable
fun Tab(
    text: String,
    pagerState: PagerState,
    index: Int,
    counter: Int? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    Tab(
        text = text,
        selected = pagerState.currentPage == index,
        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
        counter = counter,
    )
}
