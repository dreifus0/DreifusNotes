@file:Suppress("MatchingDeclarationName")

package com.dreifus.template.uikit.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.remember

class RecompositionCounter(var value: Int)

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun LogCompositions(tag: String, msg: String) {
    val recompositionCounter = remember { RecompositionCounter(0) }
    val scope = currentRecomposeScope.hashCode()
    println("$tag: $msg ${recompositionCounter.value}, scope hash: $scope")
    recompositionCounter.value++
}
