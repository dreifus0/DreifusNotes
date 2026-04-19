package com.dreifus.app.features.notes.detail

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePicker(onResult: (uri: String?) -> Unit): () -> Unit = { onResult(null) }
