package com.dreifus.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberPermissionRequester(): PermissionRequester = remember {
    // The iOS photo picker (PHPicker) runs out-of-process and needs no runtime permission.
    object : PermissionRequester {
        override fun request(permission: AppPermission, onResult: (Boolean) -> Unit) {
            onResult(true)
        }
    }
}
