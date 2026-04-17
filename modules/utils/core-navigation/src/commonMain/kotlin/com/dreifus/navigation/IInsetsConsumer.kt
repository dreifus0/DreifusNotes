package com.dreifus.navigation

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier

interface IInsetsConsumer {
    val statusBarConsumed: Boolean get() = false
    val imeConsumed: Boolean get() = false
    val navigationBarsConsumed: Boolean get() = false
}

internal fun Modifier.statusBarsPaddingIfNeeded(screen: IInsetsConsumer?): Modifier {
    return this.then(
        Modifier.run {
            if (screen?.statusBarConsumed != true) {
                this.statusBarsPadding()
            } else {
                this
            }
        }
    )
}

internal fun Modifier.imePaddingIfNeeded(screen: IInsetsConsumer?): Modifier {
    return this.then(
        Modifier.run {
            if (screen?.imeConsumed != true) {
                this.imePadding()
            } else {
                this
            }
        }
    )
}

internal fun Modifier.navigationBarsPaddingIfNeeded(screen: IInsetsConsumer?): Modifier {
    return this.then(
        Modifier.run {
            if (screen?.navigationBarsConsumed != true) {
                this.navigationBarsPadding()
            } else {
                this
            }
        }
    )
}
