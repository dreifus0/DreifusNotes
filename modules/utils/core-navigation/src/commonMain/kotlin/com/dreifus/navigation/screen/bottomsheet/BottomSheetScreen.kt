package com.dreifus.navigation.screen.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.dreifus.navigation.screen.BaseScreen
import com.dreifus.navigation.screen.bottomsheet.BottomSheetSceneStrategy.Companion.bottomSheet
import com.dreifus.template.uikit.dialog.BottomSheetDialog

interface BottomSheetScreen : BaseScreen {
    override fun <T : BaseScreen> navEntry() = navEntry<T>(
        metadata = @OptIn(ExperimentalMaterial3Api::class) BottomSheetSceneStrategy.Companion.bottomSheet(),
    ) { screen ->
        Surface(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .testTag(screen::class.simpleName!!)
        ) {
            screen.Content()
        }
    }
}

/** An [androidx.navigation3.scene.OverlayScene] that renders an [entry] within a [androidx.compose.material3.ModalBottomSheet]. */
@OptIn(ExperimentalMaterial3Api::class)
internal class BottomSheetScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        BottomSheetDialog(
            onDismissed = onBack,
        ) {
            entry.Content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull()
        val bottomSheetProperties =
            lastEntry?.metadata?.get(BOTTOM_SHEET_KEY) as? ModalBottomSheetProperties
        return bottomSheetProperties?.let {
            @Suppress("UNCHECKED_CAST")
            (BottomSheetScene(
                key = lastEntry.contentKey as T,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                onBack = onBack
            ))
        }
    }

    companion object {
        internal const val BOTTOM_SHEET_KEY = "bottomsheet"

        @OptIn(ExperimentalMaterial3Api::class)
        fun bottomSheet(
            modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
        ): Map<String, Any> = mapOf(BOTTOM_SHEET_KEY to modalBottomSheetProperties)
    }
}
