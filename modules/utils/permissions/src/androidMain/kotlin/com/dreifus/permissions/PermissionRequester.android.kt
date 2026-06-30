package com.dreifus.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberPermissionRequester(): PermissionRequester {
    val context = LocalContext.current
    // The launcher result is delivered asynchronously, so keep the pending callback.
    val pendingCallback = remember { arrayOfNulls<(Boolean) -> Unit>(1) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        pendingCallback[0]?.invoke(granted)
        pendingCallback[0] = null
    }
    return remember(launcher) {
        object : PermissionRequester {
            override fun request(permission: AppPermission, onResult: (Boolean) -> Unit) {
                val manifestPermission = permission.manifestPermission()
                if (manifestPermission == null) {
                    onResult(true)
                    return
                }
                val alreadyGranted = context.checkSelfPermission(manifestPermission) ==
                    PackageManager.PERMISSION_GRANTED
                if (alreadyGranted) {
                    onResult(true)
                } else {
                    pendingCallback[0] = onResult
                    launcher.launch(manifestPermission)
                }
            }
        }
    }
}

private fun AppPermission.manifestPermission(): String? = when (this) {
    AppPermission.Gallery -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}
