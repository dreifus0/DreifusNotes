package com.dreifus.navigation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.dreifus.navigation.IInsetsConsumer
import com.dreifus.navigation.controller.LocalTabNavState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.navigationBarsPaddingIfNeeded
import com.dreifus.navigation.screen.BaseScreen
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.navigation.statusBarsPaddingIfNeeded
import com.dreifus.template.uikit.tabs.TabBar
import com.dreifus.template.uikit.tabs.TabInfo
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.rememberHazeState
import kotlin.reflect.KClass

interface RootScreenWithTabs : RegularScreen {
    override fun <T : BaseScreen> navEntry() = navEntry<T> { screen ->
        val tabs = LocalTabs.current
        val tabNavState = LocalTabNavState.current
        val currentTab = remember(tabs, tabNavState?.activeTabIndex, screen) {
            tabNavState?.let { tabs.getOrNull(it.activeTabIndex) }
                ?: tabs.find { it.data.screenClass == screen::class }
        }
        val hazeState = rememberHazeState()
        val localDensity = LocalDensity.current
        var consumePaddingDp by remember {
            mutableStateOf(0.dp)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPaddingIfNeeded(screen as? IInsetsConsumer)
                .navigationBarsPaddingIfNeeded(screen as? IInsetsConsumer)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag(screen::class.simpleName!!)
                    .consumeWindowInsets(PaddingValues(bottom = consumePaddingDp))
                    .imePadding()
                    .haze(hazeState)
            ) {
                screen.Content()
            }
            val nav = Navigation.regular
            TabBar(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    consumePaddingDp = with(localDensity) { coordinates.size.height.toDp() }
                },
                tabs = tabs,
                hazeState = hazeState,
                currentTab = currentTab,
                onTabClick = { clickedTab ->
                    if (tabNavState != null) {
                        val targetIndex = tabs.indexOf(clickedTab)
                        if (targetIndex == tabNavState.activeTabIndex) {
                            tabNavState.popToRoot(nav)
                        } else {
                            tabNavState.switchTab(nav, targetIndex)
                        }
                    } else {
                        nav.replaceAll(clickedTab.data.screenFactory())
                    }
                },
            )
        }
    }

    interface TabData {
        val screenFactory: () -> RegularScreen
        val screenClass: KClass<out RegularScreen>
    }
}

val LocalTabs: ProvidableCompositionLocal<List<TabInfo<RootScreenWithTabs.TabData>>> =
    staticCompositionLocalOf { error("LocalTabs is not provided") }
