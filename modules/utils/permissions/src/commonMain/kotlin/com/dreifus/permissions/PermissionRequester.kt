package com.dreifus.permissions

import androidx.compose.runtime.Composable

/** Permissions the app may request at runtime. */
enum class AppPermission {
    /** Read images from the device gallery. */
    Gallery,
}

/** Requests runtime permissions and reports the result. */
interface PermissionRequester {
    fun request(permission: AppPermission, onResult: (granted: Boolean) -> Unit)
}

/**
 * Remembers a [PermissionRequester] bound to the current composition.
 * Must be created within a composable so the underlying platform launcher is registered.
 */
@Composable
expect fun rememberPermissionRequester(): PermissionRequester
