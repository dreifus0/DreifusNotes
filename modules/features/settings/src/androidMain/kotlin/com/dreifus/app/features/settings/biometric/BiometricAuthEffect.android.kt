package com.dreifus.app.features.settings.biometric

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun BiometricAuthEffect(
    trigger: Boolean,
    title: String,
    subtitle: String,
    onAuthenticated: () -> Unit,
    onDismissed: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(trigger) {
        if (!trigger) return@LaunchedEffect
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            onAuthenticated()
            return@LaunchedEffect
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val manager = context.getSystemService(android.hardware.biometrics.BiometricManager::class.java)
            if (manager == null || manager.canAuthenticate() != android.hardware.biometrics.BiometricManager.BIOMETRIC_SUCCESS) {
                onAuthenticated()
                return@LaunchedEffect
            }
        }
        showBiometricPrompt(
            context = context,
            title = title,
            subtitle = subtitle,
            onAuthenticated = onAuthenticated,
            onDismissed = onDismissed,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.P)
private fun showBiometricPrompt(
    context: android.content.Context,
    title: String,
    subtitle: String,
    onAuthenticated: () -> Unit,
    onDismissed: () -> Unit,
) {
    val executor = ContextCompat.getMainExecutor(context)
    val cancellationSignal = CancellationSignal().apply {
        setOnCancelListener { onDismissed() }
    }
    val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            onAuthenticated()
        }
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            when (errorCode) {
                BiometricPrompt.BIOMETRIC_ERROR_HW_UNAVAILABLE,
                BiometricPrompt.BIOMETRIC_ERROR_NO_BIOMETRICS -> onAuthenticated()
                else -> onDismissed()
            }
        }
    }
    BiometricPrompt.Builder(context)
        .setTitle(title)
        .setSubtitle(subtitle)
        .setNegativeButton("Cancel", executor) { _, _ -> onDismissed() }
        .build()
        .authenticate(cancellationSignal, executor, callback)
}
