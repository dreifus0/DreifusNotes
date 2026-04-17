package com.dreifus.app.features.notes.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.notes.create.mvu.CreateNoteEffect
import com.dreifus.app.features.notes.create.mvu.CreateNoteEvent
import com.dreifus.app.features.notes.create.mvu.CreateNoteState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.bottomsheet.BottomSheetScreen
import com.dreifus.template.uikit.button.AppButton
import com.dreifus.template.uikit.button.ButtonStatus
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.NoteCardColor
import com.dreifus.template.uikit.textField.AppTextField
import dev.zacsweers.metrox.viewmodel.metroViewModel

class CreateNoteScreen : BottomSheetScreen {

    @Composable
    override fun Content() {
        val vm = metroViewModel<CreateNoteViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val bottomSheetNav = Navigation.bottomSheet

        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    CreateNoteEffect.Close -> bottomSheetNav.pop()
                }
            }
        }

        CreateNoteContent(
            state = state,
            onEvent = vm::dispatch,
        )
    }
}

@Composable
private fun CreateNoteContent(
    state: CreateNoteState,
    onEvent: (CreateNoteEvent.Ui) -> Unit,
) {
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
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = "New note",
                style = AppTheme.typography.heading4,
                color = AppTheme.colors.contentPrimary,
            )

            AppTextField(
                value = state.title,
                onValueChange = { onEvent(CreateNoteEvent.Ui.TitleChanged(it)) },
                labelText = "Title",
            )

            AppTextField(
                value = state.description,
                onValueChange = { onEvent(CreateNoteEvent.Ui.DescriptionChanged(it)) },
                labelText = "Description",
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Color",
                    style = AppTheme.typography.headlineSmall,
                    color = AppTheme.colors.contentSecondary,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    NoteCardColor.entries.forEach { color ->
                        ColorDot(
                            color = color,
                            isSelected = state.selectedColor == color,
                            onClick = { onEvent(CreateNoteEvent.Ui.ColorSelected(color)) },
                        )
                    }
                }
            }

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Create",
                status = when {
                    state.isCreating -> ButtonStatus.Loading
                    state.title.isBlank() -> ButtonStatus.Disabled
                    else -> ButtonStatus.Enabled
                },
                onClick = { onEvent(CreateNoteEvent.Ui.CreateClick) },
            )
        }
    }
}

@Composable
private fun ColorDot(
    color: NoteCardColor,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val palette = color.palette()
    Box(
        modifier = Modifier
            .size(36.dp)
            .then(
                if (isSelected) Modifier.border(2.dp, AppTheme.colors.accentPrimary, CircleShape)
                else Modifier
            )
            .padding(if (isSelected) 4.dp else 0.dp)
            .clip(CircleShape)
            .background(palette.background)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
    )
}


@Preview
@Composable
private fun PreviewCreateNote() {
    AppPreview {
        CreateNoteContent(
            state = CreateNoteState(
                title = "Weekend trip plans",
                selectedColor = NoteCardColor.Purple
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewCreateNoteEmpty() {
    AppPreview {
        CreateNoteContent(
            state = CreateNoteState(),
            onEvent = {},
        )
    }
}
