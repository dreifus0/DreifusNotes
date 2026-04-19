package com.dreifus.app.features.settings.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import kotlin.coroutines.resume

@Composable
actual fun BiometricAuthEffect(
    trigger: Boolean,
    title: String,
    subtitle: String,
    onAuthenticated: () -> Unit,
    onDismissed: () -> Unit,
) {
    LaunchedEffect(trigger) {
        if (!trigger) return@LaunchedEffect
        val laContext = LAContext()
        val canAuth = laContext.canEvaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            error = null,
        )
        if (!canAuth) {
            onDismissed()
            return@LaunchedEffect
        }
        val granted = suspendCancellableCoroutine { cont ->
            laContext.evaluatePolicy(
                LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                localizedReason = subtitle,
            ) { success, _ ->
                cont.resume(success)
            }
        }
        if (granted) onAuthenticated() else onDismissed()
    }
}
