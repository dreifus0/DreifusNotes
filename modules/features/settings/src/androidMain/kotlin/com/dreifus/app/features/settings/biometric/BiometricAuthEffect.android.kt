package com.dreifus.app.features.settings.biometric

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun BiometricAuthEffect(
    trigger: Boolean,
    title: String,
    subtitle: String,
    onAuthenticated: () -> Unit,
    onDismissed: () -> Unit,
    onNoCredential: () -> Unit,
) {
    val context = LocalContext.current
    // The callbacks may change between recompositions, so always read the latest ones.
    val currentOnAuthenticated by rememberUpdatedState(onAuthenticated)
    val currentOnDismissed by rememberUpdatedState(onDismissed)
    val currentOnNoCredential by rememberUpdatedState(onNoCredential)
    val credentialLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) currentOnAuthenticated() else currentOnDismissed()
    }
    LaunchedEffect(trigger) {
        if (!trigger) return@LaunchedEffect
        val keyguard = context.getSystemService(KeyguardManager::class.java)
        if (keyguard == null || !keyguard.isDeviceSecure) {
            // No PIN/pattern/biometric lock is set, so there is no credential to verify against;
            // fall back to an in-app confirmation dialog instead of resetting silently.
            currentOnNoCredential()
            return@LaunchedEffect
        }
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> showBiometricPrompt(
                context = context,
                title = title,
                subtitle = subtitle,
                onAuthenticated = currentOnAuthenticated,
                onDismissed = currentOnDismissed,
            )
            Build.VERSION.SDK_INT == Build.VERSION_CODES.P -> showBiometricPromptLegacy(
                context = context,
                title = title,
                subtitle = subtitle,
                onAuthenticated = currentOnAuthenticated,
                onDismissed = currentOnDismissed,
                onFallbackToCredential = {
                    launchDeviceCredential(keyguard, title, subtitle, credentialLauncher::launch, currentOnDismissed)
                },
            )
            else -> launchDeviceCredential(keyguard, title, subtitle, credentialLauncher::launch, currentOnDismissed)
        }
    }
}

/**
 * API 29+: a single prompt that accepts a strong biometric **or** the device credential
 * (PIN/pattern/password), so the action is always gated behind a real authentication.
 */
@RequiresApi(Build.VERSION_CODES.Q)
private fun showBiometricPrompt(
    context: Context,
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
            onDismissed()
        }
    }
    val builder = BiometricPrompt.Builder(context)
        .setTitle(title)
        .setSubtitle(subtitle)
    // When a device credential is allowed a negative button must NOT be set.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        builder.setAllowedAuthenticators(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL,
        )
    } else {
        @Suppress("DEPRECATION")
        builder.setDeviceCredentialAllowed(true)
    }
    builder.build().authenticate(cancellationSignal, executor, callback)
}

/**
 * API 28: the framework prompt cannot offer a device-credential button, so show a biometric-only
 * prompt and fall back to the credential screen when no biometric is enrolled/available.
 */
@RequiresApi(Build.VERSION_CODES.P)
private fun showBiometricPromptLegacy(
    context: Context,
    title: String,
    subtitle: String,
    onAuthenticated: () -> Unit,
    onDismissed: () -> Unit,
    onFallbackToCredential: () -> Unit,
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
                BiometricPrompt.BIOMETRIC_ERROR_HW_NOT_PRESENT,
                BiometricPrompt.BIOMETRIC_ERROR_NO_BIOMETRICS -> onFallbackToCredential()
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

private fun launchDeviceCredential(
    keyguard: KeyguardManager,
    title: String,
    subtitle: String,
    launch: (Intent) -> Unit,
    onNoCredential: () -> Unit,
) {
    val intent = keyguard.createConfirmDeviceCredentialIntent(title, subtitle)
    if (intent != null) launch(intent) else onNoCredential()
}
