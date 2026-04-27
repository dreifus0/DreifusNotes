package com.dreifus.template.uikit.pin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreifus.template.uikit.icon.Backspace24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme

private val keypadKeys = listOf(
    "1", "2", "3",
    "4", "5", "6",
    "7", "8", "9",
    "", "0", "⌫",
)

@Composable
fun PinDots(
    filledCount: Int,
    totalCount: Int = 4,
    modifier: Modifier = Modifier,
    dotColor: androidx.compose.ui.graphics.Color = AppTheme.colors.accentPrimary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(totalCount) { index ->
            val filled = index < filledCount
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .then(
                        if (filled) Modifier.background(dotColor)
                        else Modifier.border(1.2.dp, AppTheme.colors.contentBorder, CircleShape)
                    ),
            )
        }
    }
}

@Composable
fun PinKeypad(
    onKey: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        userScrollEnabled = false,
    ) {
        items(keypadKeys) { key ->
            PinKey(key = key, onKey = onKey, onBackspace = onBackspace)
        }
    }
}

@Composable
private fun PinKey(
    key: String,
    onKey: (String) -> Unit,
    onBackspace: () -> Unit,
) {
    val isGhost = key.isEmpty()
    val isBackspace = key == "⌫"

    Box(
        modifier = Modifier.aspectRatio(2f),
        contentAlignment = Alignment.Center,
    ) {
        if (!isGhost) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(CircleShape)
                    .then(
                        if (!isBackspace) Modifier.background(AppTheme.colors.bgCardPrimary)
                        else Modifier
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = { if (isBackspace) onBackspace() else onKey(key) },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (isBackspace) {
                    AppIcons.Backspace24()
                } else {
                    Text(
                        text = key,
                        style = AppTheme.typography.heading4,
                        color = AppTheme.colors.contentPrimary,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPin() {
    AppPreview {
        Column(
            modifier = Modifier.padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            PinDots(filledCount = 2, totalCount = 4)
            PinKeypad(onKey = {}, onBackspace = {})
        }
    }
}
