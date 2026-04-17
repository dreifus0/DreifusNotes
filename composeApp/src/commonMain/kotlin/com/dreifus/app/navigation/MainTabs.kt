package com.dreifus.app.navigation

import com.dreifus.app.features.notes.main.NotesListScreen
import com.dreifus.app.features.stub.ui.StubScreen
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.navigation.ui.RootScreenWithTabs
import com.dreifus.template.uikit.icon.Notes24
import com.dreifus.template.uikit.icon.Settings24
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.tabs.TabInfo
import dreifusnotes.composeapp.generated.resources.Res
import dreifusnotes.composeapp.generated.resources.tab_notes
import dreifusnotes.composeapp.generated.resources.tab_settings
import org.jetbrains.compose.resources.stringResource
import kotlin.reflect.KClass

enum class HomeTabs(
    override val screenFactory: () -> RegularScreen,
    override val screenClass: KClass<out RegularScreen>,
) : RootScreenWithTabs.TabData {
    Notes(screenFactory = ::NotesListScreen, screenClass = NotesListScreen::class),
    Stub(screenFactory = ::StubScreen, screenClass = StubScreen::class),
}

val mainTabs: List<TabInfo<RootScreenWithTabs.TabData>> = listOf(
    TabInfo(
        icon = AppIcons.Notes24,
        title = { stringResource(Res.string.tab_notes) },
        data = HomeTabs.Notes,
    ),
    TabInfo(
        icon = AppIcons.Settings24,
        title = { stringResource(Res.string.tab_settings) },
        data = HomeTabs.Stub,
    ),
)
