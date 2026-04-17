package com.dreifus.template.uikit.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

abstract class ThemedParam<T : Any>(
    internal val defaultValue: T,
) {
    infix fun with(value: T) = this to value
}

@kotlin.jvm.JvmInline
value class ThemedParamsMap(
    private val map: Map<ThemedParam<*>, Any> = emptyMap(),
) {
    operator fun <T : Any> get(param: ThemedParam<T>) = map.get(key = param) as? T ?: param.defaultValue

    operator fun plus(other: ThemedParamsMap) = ThemedParamsMap(map + other.map)

    companion object {
        operator fun invoke(vararg pairs: Pair<ThemedParam<*>, Any>) = ThemedParamsMap(mapOf(*pairs))
    }
}

val LocalThemedParams = staticCompositionLocalOf {
    ThemedParamsMap()
}

@Composable
fun WithThemedParams(
    vararg params: Pair<ThemedParam<*>, Any>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalThemedParams provides LocalThemedParams.current + ThemedParamsMap(*params),
        content,
    )
}
