package com.dreifus.navigation.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.navigation3.runtime.NavEntry
import com.dreifus.navigation.screen.BaseScreen
import com.dreifus.navigation.screen.bottomsheet.BottomSheetScreen
import com.dreifus.navigation.screen.dialog.DialogScreen
import com.dreifus.navigation.screen.regular.RegularScreen

val LocalRegularNavController: ProvidableCompositionLocal<NavController<RegularScreen>> =
    staticCompositionLocalOf { error("LocalRegularNavController is not provided") }

val LocalDialogNavController: ProvidableCompositionLocal<NavController<DialogScreen>> =
    staticCompositionLocalOf { error("LocalDialogNavController is not provided") }

val LocalBottomSheetNavController: ProvidableCompositionLocal<NavController<BottomSheetScreen>> =
    staticCompositionLocalOf { error("LocalBottomSheetNavController is not provided") }

val LocalTabNavState: ProvidableCompositionLocal<TabNavState?> =
    staticCompositionLocalOf { null }

object Navigation {
    val regular: NavController<RegularScreen>
        @Composable
        get() = PreviewScreen.localInspectionSafeNavController {
            LocalRegularNavController.current
        }

    val dialog: NavController<DialogScreen>
        @Composable
        get() = PreviewScreen.localInspectionSafeNavController {
            LocalDialogNavController.current
        }

    val bottomSheet: NavController<BottomSheetScreen>
        @Composable
        get() = PreviewScreen.localInspectionSafeNavController {
            LocalBottomSheetNavController.current
        }

    private object PreviewScreen : RegularScreen, DialogScreen,
        BottomSheetScreen {
        @Composable
        override fun Content() = Unit

        @Composable
        inline fun <T : BaseScreen> localInspectionSafeNavController(
            normalController: @Composable () -> NavController<T>,
        ): NavController<T> = if (LocalInspectionMode.current) {
            @Suppress("UNCHECKED_CAST")
            remember { NavController(PreviewScreen) as NavController<T> }
        } else {
            normalController()
        }

        override fun <T : BaseScreen> navEntry(): NavEntry<T> = super<RegularScreen>.navEntry()
    }
}
