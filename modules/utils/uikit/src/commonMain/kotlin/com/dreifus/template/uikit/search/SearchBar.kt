package com.dreifus.template.uikit.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.icon.Search24
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme

private val SearchShape = RoundedCornerShape(8.dp)

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    onSearch: () -> Unit = {},
) {
    val colors = AppTheme.colors
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = AppTheme.typography.bodyMedium.copy(color = colors.contentPrimary),
        cursorBrush = SolidColor(colors.accentPrimary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .clip(SearchShape)
                    .background(colors.backgroundSecondary)
                    .padding(horizontal = 9.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIcons.Search24(modifier = Modifier.padding(end = 6.dp))
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = AppTheme.typography.bodyMedium,
                        color = colors.contentTertiary,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Composable
fun SearchBarReadOnly(
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    onClick: () -> Unit,
) {
    val colors = AppTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(SearchShape)
            .background(colors.backgroundSecondary)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick,
            )
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcons.Search24(modifier = Modifier.padding(end = 6.dp))
        Text(
            text = placeholder,
            style = AppTheme.typography.bodyMedium,
            color = colors.contentTertiary,
        )
    }
}
