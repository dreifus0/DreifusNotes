package com.dreifus.app.navigation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.dreifus.app.features.counter.ui.CounterScreen
import com.dreifus.app.features.stub.ui.StubScreen
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.navigation.ui.RootScreenWithTabs
import com.dreifus.template.uikit.tabs.TabInfo
import kotlin.reflect.KClass

enum class HomeTabs(
    override val screenFactory: () -> RegularScreen,
    override val screenClass: KClass<out RegularScreen>,
) : RootScreenWithTabs.TabData {
    Counter(screenFactory = ::CounterScreen, screenClass = CounterScreen::class),
    Stub(screenFactory = ::StubScreen, screenClass = StubScreen::class),
}

val mainTabs: List<TabInfo<RootScreenWithTabs.TabData>> = listOf(
    TabInfo(
        icon = { rememberVectorPainter(CounterIcon) },
        title = { "Counter" },
        data = HomeTabs.Counter,
    ),
    TabInfo(
        icon = { rememberVectorPainter(InfoIcon) },
        title = { "Stub" },
        data = HomeTabs.Stub,
    ),
)

// Material "Add" icon
private val CounterIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Counter",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 13f)
            horizontalLineTo(13f)
            verticalLineTo(19f)
            horizontalLineTo(11f)
            verticalLineTo(13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineTo(11f)
            verticalLineTo(5f)
            horizontalLineTo(13f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            close()
        }
    }.build()
}

// Material "Info" icon
private val InfoIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Info",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            reflectiveCurveTo(4.48f, 10f, 12f, 22f)
            reflectiveCurveTo(22f, 17.52f, 22f, 12f)
            reflectiveCurveTo(17.52f, 2f, 12f, 2f)
            close()
            moveTo(13f, 17f)
            horizontalLineTo(11f)
            verticalLineTo(11f)
            horizontalLineTo(13f)
            close()
            moveTo(13f, 9f)
            horizontalLineTo(11f)
            verticalLineTo(7f)
            horizontalLineTo(13f)
            close()
        }
    }.build()
}
