package com.dreifus.app.features.settings.biometric

import androidx.compose.runtime.Composable

@Composable
expect fun BiometricAuthEffect(
    trigger: Boolean,
    title: String,
    subtitle: String,
    onAuthenticated: () -> Unit,
    onDismissed: () -> Unit,
)
