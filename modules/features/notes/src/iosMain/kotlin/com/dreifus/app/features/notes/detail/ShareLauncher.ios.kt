package com.dreifus.app.features.notes.detail

import androidx.compose.runtime.Composable

@Composable
actual fun rememberShareLauncher(): (String) -> Unit = { }

@Composable
actual fun rememberShareImageLauncher(): (String) -> Unit = { }

@Composable
actual fun rememberCopyImageLauncher(): (String) -> Unit = { }
