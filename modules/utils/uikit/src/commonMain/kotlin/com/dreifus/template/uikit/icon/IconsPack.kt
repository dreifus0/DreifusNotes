package com.dreifus.template.uikit.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.cachedIcon
import com.dreifus.template.uikit.utils.wrapWith

//region:24px

val AppIcons.ArrowLeft24 by cachedIcon {
    rememberPainter(ArrowBackIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Close24 by cachedIcon {
    rememberPainter(CloseIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Plus24 by cachedIcon {
    rememberPainter(PlusIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Search24 by cachedIcon {
    rememberPainter(SearchIcon).wrapWith(tint = AppTheme.colors.contentTertiary)
}

val AppIcons.Lock24 by cachedIcon {
    rememberPainter(LockIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.ChevronRight24 by cachedIcon {
    rememberPainter(ChevronRightIcon).wrapWith(tint = AppTheme.colors.contentTertiary)
}

val AppIcons.MoreHoriz24 by cachedIcon {
    rememberPainter(MoreHorizIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Edit24 by cachedIcon {
    rememberPainter(EditIcon).wrapWith(tint = AppTheme.colors.accentOnPrimary)
}

val AppIcons.Settings24 by cachedIcon {
    rememberPainter(SettingsIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Notes24 by cachedIcon {
    rememberPainter(NotesIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Fingerprint24 by cachedIcon {
    rememberPainter(FingerprintIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Clock24 by cachedIcon {
    rememberPainter(ClockIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Theme24 by cachedIcon {
    rememberPainter(ThemeIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Palette24 by cachedIcon {
    rememberPainter(PaletteIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Download24 by cachedIcon {
    rememberPainter(DownloadIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Trash24 by cachedIcon {
    rememberPainter(TrashIcon).wrapWith(tint = AppTheme.colors.accentError)
}

val AppIcons.Backspace24 by cachedIcon {
    rememberPainter(BackspaceIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

val AppIcons.Send24 by cachedIcon {
    rememberPainter(SendIcon).wrapWith(tint = AppTheme.colors.accentOnPrimary)
}

val AppIcons.Checklist24 by cachedIcon {
    rememberPainter(ChecklistIcon).wrapWith(tint = AppTheme.colors.contentPrimary)
}

//endregion

@androidx.compose.runtime.Composable
private fun rememberPainter(vector: ImageVector) =
    androidx.compose.ui.graphics.vector.rememberVectorPainter(vector)

private fun icon24(
    name: String,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> Unit,
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
    autoMirror = autoMirror,
).apply(block).build()

private fun ImageVector.Builder.stroke(block: PathBuilder.() -> Unit) {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.8f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        pathBuilder = block,
    )
}

private fun ImageVector.Builder.fill(block: PathBuilder.() -> Unit) {
    path(fill = SolidColor(Color.Black), pathBuilder = block)
}

private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
    moveTo(cx - r, cy)
    arcTo(r, r, 0f, false, true, cx + r, cy)
    arcTo(r, r, 0f, false, true, cx - r, cy)
    close()
}

private val ArrowBackIcon: ImageVector by lazy {
    icon24(name = "ArrowBack", autoMirror = true) {
        stroke {
            moveTo(15f, 5f); lineTo(7f, 12f); lineTo(15f, 19f)
        }
    }
}

private val CloseIcon: ImageVector by lazy {
    icon24(name = "Close") {
        stroke {
            moveTo(6f, 6f); lineTo(18f, 18f)
            moveTo(18f, 6f); lineTo(6f, 18f)
        }
    }
}

private val PlusIcon: ImageVector by lazy {
    icon24(name = "Plus") {
        stroke {
            moveTo(12f, 4f); lineTo(12f, 20f)
            moveTo(4f, 12f); lineTo(20f, 12f)
        }
    }
}

private val SearchIcon: ImageVector by lazy {
    icon24(name = "Search") {
        stroke {
            circle(cx = 10.5f, cy = 10.5f, r = 7f)
            moveTo(16f, 16f); lineTo(21f, 21f)
        }
    }
}

private val LockIcon: ImageVector by lazy {
    icon24(name = "Lock") {
        stroke {
            // body
            moveTo(5f, 11f)
            lineTo(19f, 11f)
            lineTo(19f, 21f)
            lineTo(5f, 21f)
            close()
            // shackle
            moveTo(8f, 11f); lineTo(8f, 7f)
            curveTo(8f, 4.8f, 9.8f, 3f, 12f, 3f)
            curveTo(14.2f, 3f, 16f, 4.8f, 16f, 7f)
            lineTo(16f, 11f)
        }
    }
}

private val ChevronRightIcon: ImageVector by lazy {
    icon24(name = "ChevronRight", autoMirror = true) {
        stroke {
            moveTo(9f, 5f); lineTo(16f, 12f); lineTo(9f, 19f)
        }
    }
}

private val MoreHorizIcon: ImageVector by lazy {
    icon24(name = "MoreHoriz") {
        fill {
            circle(cx = 5f, cy = 12f, r = 2f)
            circle(cx = 12f, cy = 12f, r = 2f)
            circle(cx = 19f, cy = 12f, r = 2f)
        }
    }
}

private val EditIcon: ImageVector by lazy {
    icon24(name = "Edit") {
        stroke {
            moveTo(16f, 3f)
            lineTo(21f, 8f)
            lineTo(8f, 21f)
            lineTo(3f, 21f)
            lineTo(3f, 16f)
            close()
        }
    }
}

private val SettingsIcon: ImageVector by lazy {
    icon24(name = "Settings") {
        stroke {
            circle(cx = 12f, cy = 12f, r = 3f)
            // 8 rays
            moveTo(12f, 3f); lineTo(12f, 5.5f)
            moveTo(12f, 18.5f); lineTo(12f, 21f)
            moveTo(3f, 12f); lineTo(5.5f, 12f)
            moveTo(18.5f, 12f); lineTo(21f, 12f)
            moveTo(5.6f, 5.6f); lineTo(7.4f, 7.4f)
            moveTo(16.6f, 16.6f); lineTo(18.4f, 18.4f)
            moveTo(5.6f, 18.4f); lineTo(7.4f, 16.6f)
            moveTo(16.6f, 7.4f); lineTo(18.4f, 5.6f)
        }
    }
}

private val NotesIcon: ImageVector by lazy {
    icon24(name = "Notes") {
        stroke {
            moveTo(5f, 4f)
            lineTo(17f, 4f)
            curveTo(18f, 4f, 19f, 5f, 19f, 6f)
            lineTo(19f, 20f)
            lineTo(7f, 20f)
            curveTo(6f, 20f, 5f, 19f, 5f, 18f)
            close()
            moveTo(8f, 9f); lineTo(16f, 9f)
            moveTo(8f, 13f); lineTo(16f, 13f)
            moveTo(8f, 17f); lineTo(13f, 17f)
        }
    }
}

private val FingerprintIcon: ImageVector by lazy {
    icon24(name = "Fingerprint") {
        stroke {
            moveTo(4f, 9f)
            curveTo(4f, 6f, 7f, 3f, 12f, 3f)
            curveTo(17f, 3f, 20f, 6f, 20f, 9f)
            moveTo(7f, 10f)
            lineTo(7f, 14f)
            curveTo(7f, 18f, 10f, 21f, 12f, 21f)
            moveTo(12f, 9f)
            lineTo(12f, 14f)
            curveTo(12f, 17f, 14f, 20f, 17f, 21f)
        }
    }
}

private val ClockIcon: ImageVector by lazy {
    icon24(name = "Clock") {
        stroke {
            circle(cx = 12f, cy = 12f, r = 8.5f)
            moveTo(12f, 7f); lineTo(12f, 12f); lineTo(16f, 14f)
        }
    }
}

private val ThemeIcon: ImageVector by lazy {
    icon24(name = "Theme") {
        stroke {
            circle(cx = 12f, cy = 12f, r = 8f)
        }
        fill {
            moveTo(12f, 4f)
            curveTo(7.6f, 4f, 4f, 7.6f, 4f, 12f)
            curveTo(4f, 16.4f, 7.6f, 20f, 12f, 20f)
            close()
        }
    }
}

private val PaletteIcon: ImageVector by lazy {
    icon24(name = "Palette") {
        stroke {
            moveTo(12f, 3f)
            curveTo(7f, 3f, 3f, 7f, 3f, 12f)
            curveTo(3f, 17f, 6f, 18f, 8f, 18f)
            curveTo(10f, 18f, 10f, 17f, 10f, 16f)
            curveTo(10f, 15f, 11f, 14f, 12f, 14f)
            lineTo(17f, 14f)
            curveTo(19f, 14f, 22f, 13f, 22f, 10f)
            curveTo(22f, 6f, 17f, 3f, 12f, 3f)
            close()
        }
    }
}

private val DownloadIcon: ImageVector by lazy {
    icon24(name = "Download") {
        stroke {
            moveTo(12f, 4f); lineTo(12f, 14f)
            moveTo(7f, 10f); lineTo(12f, 14f); lineTo(17f, 10f)
            moveTo(4f, 18f); lineTo(20f, 18f)
        }
    }
}

private val TrashIcon: ImageVector by lazy {
    icon24(name = "Trash") {
        stroke {
            moveTo(4f, 7f); lineTo(20f, 7f)
            moveTo(10f, 4f); lineTo(14f, 4f)
            moveTo(6f, 7f)
            lineTo(6f, 19f)
            curveTo(6f, 20f, 7f, 21f, 8f, 21f)
            lineTo(16f, 21f)
            curveTo(17f, 21f, 18f, 20f, 18f, 19f)
            lineTo(18f, 7f)
            moveTo(10f, 11f); lineTo(10f, 17f)
            moveTo(14f, 11f); lineTo(14f, 17f)
        }
    }
}

private val BackspaceIcon: ImageVector by lazy {
    icon24(name = "Backspace") {
        stroke {
            moveTo(8f, 5f)
            lineTo(21f, 5f)
            lineTo(21f, 19f)
            lineTo(8f, 19f)
            lineTo(2f, 12f)
            close()
            moveTo(12f, 9f); lineTo(17f, 15f)
            moveTo(17f, 9f); lineTo(12f, 15f)
        }
    }
}

private val SendIcon: ImageVector by lazy {
    icon24(name = "Send") {
        stroke {
            moveTo(22f, 2f); lineTo(11f, 13f)
            moveTo(22f, 2f); lineTo(15f, 22f); lineTo(11f, 13f); lineTo(2f, 9f); lineTo(22f, 2f)
        }
    }
}

private val ChecklistIcon: ImageVector by lazy {
    icon24(name = "Checklist") {
        stroke {
            moveTo(3f, 7f); lineTo(5.5f, 9.5f); lineTo(8.5f, 4.5f)
            moveTo(11f, 7f); lineTo(21f, 7f)
            moveTo(3f, 12f); lineTo(5.5f, 14.5f); lineTo(8.5f, 9.5f)
            moveTo(11f, 12f); lineTo(21f, 12f)
            moveTo(3f, 17f); lineTo(5.5f, 19.5f); lineTo(8.5f, 14.5f)
            moveTo(11f, 17f); lineTo(21f, 17f)
        }
    }
}
