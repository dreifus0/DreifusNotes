package com.dreifus.app.features.notes.detail

import androidx.compose.runtime.Composable

@Composable
expect fun rememberShareLauncher(): (String) -> Unit

@Composable
expect fun rememberShareImageLauncher(): (String) -> Unit

@Composable
expect fun rememberCopyImageLauncher(): (String) -> Unit
