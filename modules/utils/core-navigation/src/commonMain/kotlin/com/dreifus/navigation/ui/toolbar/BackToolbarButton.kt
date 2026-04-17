package com.dreifus.navigation.ui.toolbar

import androidx.compose.runtime.Composable
import com.dreifus.navigation.controller.Navigation
import com.dreifus.template.uikit.style.AppIcon
import com.dreifus.template.uikit.toolbar.ToolbarButton
import com.dreifus.template.uikit.toolbar.ToolbarPosition

open class BackToolbarButton(
    override val position: ToolbarPosition,
    override val icon: AppIcon,
) : ToolbarButton {

    @Composable
    override fun onClick(): () -> Unit {
        val navController = Navigation.regular
        return {
            navController.pop()
        }
    }
}
