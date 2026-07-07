package com.dreifus.template.uikit.effect

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

private const val DURATION_MS = 700
// Particles start left to right: stagger by column plus a random jitter, both as a fraction of
// the total progress. Their sum must stay well below 1 so every particle gets time to fly.
private const val COLUMN_STAGGER = 0.3f
private const val RANDOM_JITTER = 0.2f
private const val SHRINK = 0.4f

/**
 * Crumble-away animation: while [active] the content is hidden and its last drawn frame, split
 * into a grid of particles, scatters and fades out. [onFinished] fires once the dust settles —
 * remove the item from data there. The content stays hidden after finishing (until the modifier
 * leaves composition or [active] becomes false), so the item never flashes back while its
 * removal propagates.
 */
@Composable
fun Modifier.dissolveEffect(active: Boolean, onFinished: () -> Unit): Modifier {
    val layer = rememberGraphicsLayer()
    val density = LocalDensity.current
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var particles by remember { mutableStateOf<List<Particle>>(emptyList()) }
    val progress = remember { Animatable(0f) }
    val currentOnFinished by rememberUpdatedState(onFinished)

    LaunchedEffect(active) {
        if (active) {
            val captured = layer.toImageBitmap()
            val cell = with(density) { 12.dp.roundToPx() }.coerceAtLeast(4)
            particles = generateParticles(captured.width, captured.height, cell)
            bitmap = captured
            progress.snapTo(0f)
            progress.animateTo(1f, tween(DURATION_MS, easing = LinearEasing))
            currentOnFinished()
        } else {
            bitmap = null
            particles = emptyList()
            progress.snapTo(0f)
        }
    }

    return drawWithContent {
        layer.record { this@drawWithContent.drawContent() }
        val captured = bitmap
        if (!active || captured == null) {
            // Not dissolving, or the capture is still in flight — show the live content.
            drawLayer(layer)
            return@drawWithContent
        }
        val t = progress.value
        for (particle in particles) {
            val local = ((t - particle.delay) / (1f - particle.delay)).coerceIn(0f, 1f)
            if (local >= 1f) continue
            val scale = 1f - SHRINK * local
            val dstW = (particle.srcSize.width * scale).roundToInt().coerceAtLeast(1)
            val dstH = (particle.srcSize.height * scale).roundToInt().coerceAtLeast(1)
            val dx = particle.srcOffset.x +
                particle.velocityX * local +
                (particle.srcSize.width - dstW) / 2f
            val dy = particle.srcOffset.y +
                particle.velocityY * local +
                particle.gravity * local * local +
                (particle.srcSize.height - dstH) / 2f
            drawImage(
                image = captured,
                srcOffset = particle.srcOffset,
                srcSize = particle.srcSize,
                dstOffset = IntOffset(dx.roundToInt(), dy.roundToInt()),
                dstSize = IntSize(dstW, dstH),
                alpha = 1f - local,
            )
        }
    }
}

private class Particle(
    val srcOffset: IntOffset,
    val srcSize: IntSize,
    val delay: Float,
    val velocityX: Float,
    val velocityY: Float,
    val gravity: Float,
)

private fun generateParticles(width: Int, height: Int, cell: Int): List<Particle> {
    val random = Random(width * 31 + height)
    val cols = (width + cell - 1) / cell
    val rows = (height + cell - 1) / cell
    return buildList {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val x = col * cell
                val y = row * cell
                val w = minOf(cell, width - x)
                val h = minOf(cell, height - y)
                if (w <= 0 || h <= 0) continue
                add(
                    Particle(
                        srcOffset = IntOffset(x, y),
                        srcSize = IntSize(w, h),
                        delay = col.toFloat() / cols * COLUMN_STAGGER +
                            random.nextFloat() * RANDOM_JITTER,
                        // Sideways scatter with a slight rightward bias (matches the
                        // left-to-right stagger), an upward kick, then gravity wins.
                        velocityX = (random.nextFloat() * 2.5f - 1f) * cell * 3f,
                        velocityY = -(0.5f + random.nextFloat() * 2f) * cell * 3f,
                        gravity = cell * 8f,
                    )
                )
            }
        }
    }
}
