package com.dreifus.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.dreifus.navigation.controller.LocalBottomSheetNavController
import com.dreifus.navigation.controller.LocalDialogNavController
import com.dreifus.navigation.controller.LocalRegularNavController
import com.dreifus.navigation.controller.LocalTabNavState
import com.dreifus.navigation.controller.NavControllersHolder
import com.dreifus.navigation.screen.BaseScreen
import com.dreifus.navigation.screen.bottomsheet.BottomSheetSceneStrategy

private val noContentTransform = ContentTransform(
    targetContentEnter = EnterTransition.None,
    initialContentExit = ExitTransition.None,
    sizeTransform = null,
)

@Composable
fun NavigationSetup(
    navControllersHolder: NavControllersHolder,
    listener: (BaseScreen) -> Unit = {},
    entryDecorators: List<NavEntryDecorator<BaseScreen>> = emptyList(),
) {
    val backstack by remember {
        var currentRegularScreen = navControllersHolder.regular.backstack.last()
        derivedStateOf {
            val newRegularScreen = navControllersHolder.regular.backstack.last()
            val regularScreenChanged = newRegularScreen != currentRegularScreen
            if (regularScreenChanged) {
                currentRegularScreen = newRegularScreen
                // if the screen changed, close all overlays
                navControllersHolder.bottomSheet.backstack.clear()
                navControllersHolder.dialog.backstack.clear()
            }
            val overlays =
                navControllersHolder.bottomSheet.backstack + navControllersHolder.dialog.backstack
            navControllersHolder.regular.backstack +
                    // show only the last dialog or bottom sheet (limitation of current scene implementations)
                    listOfNotNull(overlays.lastOrNull())
        }
    }
    val currentScreen by remember { derivedStateOf { backstack.last() } }
    LaunchedEffect(currentScreen) {
        listener(currentScreen)
    }

    CompositionLocalProvider(
        LocalRegularNavController provides navControllersHolder.regular,
        LocalDialogNavController provides navControllersHolder.dialog,
        LocalBottomSheetNavController provides navControllersHolder.bottomSheet,
        LocalTabNavState provides navControllersHolder.tabNavState,
    ) {
        NavDisplay(
            backStack = backstack,
            sceneStrategy = remember {
                DialogSceneStrategy<BaseScreen>() then BottomSheetSceneStrategy()
            },
            entryDecorators = entryDecorators + listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            onBack = {
                when {
                    navControllersHolder.bottomSheet.backstack.isNotEmpty() -> navControllersHolder.bottomSheet.pop()
                    navControllersHolder.dialog.backstack.isNotEmpty() -> navControllersHolder.dialog.pop()
                    else -> {
                        val tabState = navControllersHolder.tabNavState
                        if (tabState != null) {
                            val regular = navControllersHolder.regular
                            val activeRootPos = regular.backstack.indexOf(tabState.activeRoot)
                            when {
                                activeRootPos < regular.backstack.size - 1 ->
                                    regular.pop() // has inner screens → pop
                                tabState.activeTabIndex != 0 ->
                                    tabState.switchTab(regular, 0) // non-primary → go to primary
                                else ->
                                    regular.pop() // primary root → system back
                            }
                        } else {
                            navControllersHolder.regular.pop()
                        }
                    }
                }
            },
            transitionSpec = { noContentTransform },
            popTransitionSpec = { noContentTransform },
        ) {
            it.navEntry()
        }
    }
}
