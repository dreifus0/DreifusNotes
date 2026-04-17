package com.dreifus.navigation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavEntry
import dev.zacsweers.metro.MapKey
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Stable
interface BaseScreen {

    @Composable
    fun Content()

    fun getScreenTrackInfo(): ScreenTrackInfo? = null

    fun <T : BaseScreen> navEntry(
        metadata: Map<String, Any> = emptyMap(),
        content: @Composable (T) -> Unit,
    ) = NavEntry(
        key = this as T,
        contentKey = this.toString(),
        metadata = metadata,
        content = content,
    )

    fun <T : BaseScreen> navEntry(): NavEntry<T>
}

interface IScreenWithDynamicParams {
    val dynamicParamsMap: MutableMap<String, Any?>
}

/**
 * For exchanging data between screens via navigation stack.
 */
abstract class ScreenWithDynamicParams : IScreenWithDynamicParams {
    override val dynamicParamsMap: MutableMap<String, Any?> = mutableMapOf()
}

abstract class ScreenWithResult : ScreenWithDynamicParams() {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> resultState(): MutableState<T?> =
        dynamicParamsMap.getOrPut("result_" + T::class.simpleName) {
            mutableStateOf(null)
        } as MutableState<T?>
}

@Serializable
data class ScreenTrackInfo(
    val name: String? = null, // default - class name without suffix "Screen"
    val valueTeam: String? = null,
    val others: Map<String, String> = emptyMap(),
)

/**
 * For overriding default screen info description for analytics.
 */
annotation class ScreenInfo(
    val name: String = "", // default - class name without suffix "Screen"
    val valueTeam: String = "", // default - value from gradle plugin trackingInfo { valueTeam = ... }
)

typealias ScreensMap = Map<KClass<out BaseScreen>, ScreenInfo>

@MapKey
annotation class ScreenClassKey(
    val value: KClass<out BaseScreen>,
)
