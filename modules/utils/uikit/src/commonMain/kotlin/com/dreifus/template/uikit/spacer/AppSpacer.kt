@file:Suppress("NOTHING_TO_INLINE", "PackageDirectoryMismatch")

package androidx.compose.foundation.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

@Composable
@NonRestartableComposable
inline fun Spacer(size: Dp) {
    Spacer(modifier = Modifier.size(size))
}

@Composable
@NonRestartableComposable
inline fun ColumnScope.Spacer(weight: Float = 1f) {
    Spacer(modifier = Modifier.weight(weight))
}

@Composable
@NonRestartableComposable
inline fun RowScope.Spacer(weight: Float = 1f) {
    Spacer(modifier = Modifier.weight(weight))
}

@Composable
@NonRestartableComposable
fun Spacer(modifier: Modifier) {
    Layout(measurePolicy = AppSpacerMeasurePolicy, modifier = modifier)
}

private object AppSpacerMeasurePolicy : MeasurePolicy {

    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        return with(constraints) {
            val width = if (hasFixedWidth) maxWidth else 0
            val height = if (hasFixedHeight) maxHeight else 0
            layout(width, height) {}
        }
    }
}
