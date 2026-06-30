package com.dreifus.app.features.notes.detail

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.File

@Composable
actual fun rememberImagePicker(onResult: (uri: String?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        // The picker grants only a temporary read permission for the content URI, so copy the bytes
        // into app-internal storage and hand back a stable file:// path that survives app restarts.
        onResult(uri?.let { context.persistPickedImage(it) })
    }
    return {
        launcher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}

private fun Context.persistPickedImage(uri: Uri): String? = runCatching {
    val imagesDir = File(filesDir, "images").apply { mkdirs() }
    val target = File(imagesDir, "img_${System.currentTimeMillis()}.jpg")
    contentResolver.openInputStream(uri)?.use { input ->
        target.outputStream().use { output -> input.copyTo(output) }
    } ?: return null
    "file://${target.absolutePath}"
}.getOrNull()
