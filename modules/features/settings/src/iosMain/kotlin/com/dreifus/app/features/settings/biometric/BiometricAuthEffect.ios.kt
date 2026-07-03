package com.dreifus.app.features.settings.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthentication
import kotlin.coroutines.resume

@Composable
actual fun BiometricAuthEffect(
    trigger: Boolean,
    title: String,
    subtitle: String,
    onAuthenticated: () -> Unit,
    onDismissed: () -> Unit,
    onNoCredential: () -> Unit,
) {
    LaunchedEffect(trigger) {
        if (!trigger) return@LaunchedEffect
        val laContext = LAContext()
        // Device owner authentication = biometrics with an automatic passcode fallback.
        val canAuth = laContext.canEvaluatePolicy(
            LAPolicyDeviceOwnerAuthentication,
            error = null,
        )
        if (!canAuth) {
            // No passcode/biometrics enrolled: fall back to an in-app confirmation dialog.
            onNoCredential()
            return@LaunchedEffect
        }
        val granted = suspendCancellableCoroutine { cont ->
            laContext.evaluatePolicy(
                LAPolicyDeviceOwnerAuthentication,
                localizedReason = subtitle,
            ) { success, _ ->
                cont.resume(success)
            }
        }
        if (granted) onAuthenticated() else onDismissed()
    }
}
