package com.dreifus.navigation.controller

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.dreifus.navigation.screen.regular.RegularScreen
import kotlin.reflect.KClass

@Stable
class TabNavState(
    private val tabRoots: List<RegularScreen>,
    initialActiveIndex: Int = 0,
) {
    private val savedInnerStacks = mutableMapOf<KClass<out RegularScreen>, List<RegularScreen>>()

    var activeTabIndex: Int by mutableIntStateOf(initialActiveIndex)
        private set

    val activeRoot: RegularScreen get() = tabRoots[activeTabIndex]
    val tabCount: Int get() = tabRoots.size

    /** Builds the initial backstack: [inactive roots..., active root] */
    fun buildInitialBackstack(): List<RegularScreen> {
        val active = tabRoots[activeTabIndex]
        val inactive = tabRoots.filter { it !== active }
        return inactive + active
    }

    /** Switch to a different tab. Saves current inner stack, restores target's. */
    fun switchTab(navController: NavController<RegularScreen>, targetIndex: Int) {
        if (targetIndex == activeTabIndex) return

        // Save current tab's inner screens (everything above tab roots)
        val backstack = navController.backstack.toList()
        val activeRootPos = backstack.indexOf(activeRoot)
        if (activeRootPos >= 0 && activeRootPos < backstack.size - 1) {
            savedInnerStacks[activeRoot::class] =
                backstack.subList(activeRootPos + 1, backstack.size)
        } else {
            savedInnerStacks.remove(activeRoot::class)
        }

        // Switch
        activeTabIndex = targetIndex
        val newActive = tabRoots[targetIndex]
        val inactive = tabRoots.filter { it !== newActive }
        val restoredInner = savedInnerStacks[newActive::class] ?: emptyList()

        // Atomic swap — same instances → ViewModelStores preserved
        navController.replaceAll(inactive + newActive + restoredInner)
    }

    /** Pop inner stack to tab root (double-tap on active tab) */
    fun popToRoot(navController: NavController<RegularScreen>) {
        savedInnerStacks.remove(activeRoot::class)
        val active = tabRoots[activeTabIndex]
        val inactive = tabRoots.filter { it !== active }
        navController.replaceAll(inactive + active)
    }

    fun isTabRoot(screen: RegularScreen): Boolean =
        tabRoots.any { it === screen }
}
