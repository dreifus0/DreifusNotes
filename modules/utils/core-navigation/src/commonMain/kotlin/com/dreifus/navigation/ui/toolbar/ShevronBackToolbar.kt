package com.dreifus.navigation.ui.toolbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.toolbar.AppToolbar
import com.dreifus.template.uikit.toolbar.ToolbarPosition


@Composable
fun ShevronBackToolbar(
    title: String,
    modifier: Modifier = Modifier,
) {
    AppToolbar(
        title = title,
        modifier = modifier,
        button = ShevronBackToolbarButton,
    )
}

object ShevronBackToolbarButton : BackToolbarButton(ToolbarPosition.START, AppIcons.ArrowLeft24)
