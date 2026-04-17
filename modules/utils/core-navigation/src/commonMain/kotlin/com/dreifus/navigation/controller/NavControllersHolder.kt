package com.dreifus.navigation.controller

import com.dreifus.navigation.screen.bottomsheet.BottomSheetScreen
import com.dreifus.navigation.screen.dialog.DialogScreen
import com.dreifus.navigation.screen.regular.RegularScreen

class NavControllersHolder(
    rootScreen: RegularScreen? = null,
    val tabNavState: TabNavState? = null,
) {
    val regular: NavController<RegularScreen> = when {
        tabNavState != null -> NavController(
            *tabNavState.buildInitialBackstack().toTypedArray(),
            isEmptyBackstackPossible = false
        )

        rootScreen != null -> NavController(
            rootScreen,
            isEmptyBackstackPossible = false
        )

        else -> error("Either rootScreen or tabNavState must be provided")
    }
    val dialog = NavController<DialogScreen>(filterNavigationToSameClass = true)
    val bottomSheet = NavController<BottomSheetScreen>(filterNavigationToSameClass = true)
}
