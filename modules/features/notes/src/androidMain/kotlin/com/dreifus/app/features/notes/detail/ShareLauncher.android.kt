package com.dreifus.app.features.notes.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
actual fun rememberShareLauncher(): (String) -> Unit {
    val context = LocalContext.current
    return { text ->
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, null))
    }
}

@Composable
actual fun rememberShareImageLauncher(): (String) -> Unit {
    val context = LocalContext.current
    return { uri ->
        context.toShareableUri(uri)?.let { contentUri ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, null))
        }
    }
}

@Composable
actual fun rememberCopyImageLauncher(): (String) -> Unit {
    val context = LocalContext.current
    return { uri ->
        context.toShareableUri(uri)?.let { contentUri ->
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newUri(context.contentResolver, "image", contentUri))
        }
    }
}

// Picked photos live as file:// paths in app-internal storage, unreadable by other apps —
// re-expose them as content:// through the FileProvider declared in this module's manifest.
private fun Context.toShareableUri(uri: String): Uri? = runCatching {
    val parsed = Uri.parse(uri)
    if (parsed.scheme == "file") {
        FileProvider.getUriForFile(this, "$packageName.fileprovider", File(parsed.path!!))
    } else {
        parsed
    }
}.getOrNull()
