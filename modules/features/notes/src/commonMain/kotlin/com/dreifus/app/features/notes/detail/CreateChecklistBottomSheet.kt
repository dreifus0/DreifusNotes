package com.dreifus.app.features.notes.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.bottomsheet.BottomSheetScreen
import com.dreifus.template.uikit.button.AppButton
import com.dreifus.template.uikit.button.ButtonStatus
import com.dreifus.template.uikit.icon.Close24
import com.dreifus.template.uikit.icon.GlassIcon
import com.dreifus.template.uikit.icon.Plus24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.textField.AppTextField

class CreateChecklistBottomSheet(
    private val onConfirm: (title: String, items: List<String>) -> Unit,
) : BottomSheetScreen {

    @Composable
    override fun Content() {
        val bottomSheetNav = Navigation.bottomSheet
        CreateChecklistContent(
            onConfirm = { title, items ->
                onConfirm(title, items)
                bottomSheetNav.pop()
            },
        )
    }
}

@Composable
private fun CreateChecklistContent(onConfirm: (title: String, items: List<String>) -> Unit) {
    var titleText by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(emptyList<String>()) }

    fun addCurrentItem() {
        val trimmed = inputText.trim()
        if (trimmed.isNotBlank()) {
            items = items + trimmed
            inputText = ""
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AppTheme.colors.contentDividers),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "New checklist",
                style = AppTheme.typography.heading4,
                color = AppTheme.colors.contentPrimary,
            )

            AppTextField(
                value = titleText,
                onValueChange = { titleText = it },
                labelText = "Title (optional)",
                imeAction = ImeAction.Next,
            )

            if (items.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            CheckboxDot()
                            Text(
                                text = item,
                                modifier = Modifier.weight(1f),
                                style = AppTheme.typography.bodyLarge,
                                color = AppTheme.colors.contentPrimary,
                            )
                            GlassIcon(
                                icon = AppIcons.Close24,
                                onClick = {
                                    items = items.toMutableList().also { it.removeAt(index) }
                                },
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AppTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    labelText = "Item text…",
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { addCurrentItem() }),
                )
                GlassIcon(
                    icon = AppIcons.Plus24,
                    onClick = { addCurrentItem() },
                )
            }

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Add to note",
                status = if (items.isEmpty()) ButtonStatus.Disabled else ButtonStatus.Enabled,
                onClick = { onConfirm(titleText.trim(), items) },
            )
        }
    }
}

@Composable
internal fun CheckboxDot() {
    Box(
        modifier = Modifier
            .size(18.dp)
            .border(1.5.dp, AppTheme.colors.contentTertiary, RoundedCornerShape(4.dp)),
    )
}

@Preview
@Composable
private fun PreviewCreateChecklist() {
    AppPreview {
        CreateChecklistContent(onConfirm = { _, _ -> })
    }
}

@Preview
@Composable
private fun PreviewCreateChecklistWithItems() {
    AppPreview {
        CreateChecklistContent(onConfirm = { _, _ -> })
    }
}
