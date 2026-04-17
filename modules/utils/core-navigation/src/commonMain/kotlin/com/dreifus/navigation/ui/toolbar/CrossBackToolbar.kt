package com.dreifus.navigation.ui.toolbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dreifus.template.uikit.icon.Close24
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.toolbar.AppToolbar
import com.dreifus.template.uikit.toolbar.ToolbarPosition


@Composable
fun CrossBackToolbar(
    title: String,
    modifier: Modifier = Modifier,
    onCrossClick: (() -> Unit)? = null,
) {
    AppToolbar(
        title = title,
        modifier = modifier,
        button = if (onCrossClick == null) {
            CrossBackToolbarButton
        } else {
            object :
                BackToolbarButton(CrossBackToolbarButton.position, CrossBackToolbarButton.icon) {
                @Composable
                override fun onClick(): () -> Unit {
                    return onCrossClick
                }
            }
        },
    )
}

object CrossBackToolbarButton : BackToolbarButton(ToolbarPosition.END, AppIcons.Close24)
