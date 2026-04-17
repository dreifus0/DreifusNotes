@file:Suppress("NOTHING_TO_INLINE")

package com.dreifus.template.uikit.button

enum class ButtonStatus {
    Enabled, Disabled, Loading;
}

inline fun ButtonStatus.isEnabled() = this == ButtonStatus.Enabled
inline fun ButtonStatus.isLoading() = this == ButtonStatus.Loading
