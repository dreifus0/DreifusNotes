package com.dreifus.app.features.events.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.events.edit.mvu.EditEventEffect
import com.dreifus.app.features.events.edit.mvu.EditEventEvent
import com.dreifus.app.features.events.edit.mvu.EditEventState
import com.dreifus.app.features.events.format.EventDateFormat
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.bottomsheet.BottomSheetScreen
import com.dreifus.template.uikit.button.AppButton
import com.dreifus.template.uikit.button.ButtonStatus
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.row.AppGroup
import com.dreifus.template.uikit.row.AppRow
import com.dreifus.template.uikit.row.AppSectionLabel
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.textField.AppTextField
import dev.zacsweers.metrox.viewmodel.metroViewModel
import dreifusnotes.modules.features.events.generated.resources.Res
import dreifusnotes.modules.features.events.generated.resources.event_cancel
import dreifusnotes.modules.features.events.generated.resources.event_create_button
import dreifusnotes.modules.features.events.generated.resources.event_date_label
import dreifusnotes.modules.features.events.generated.resources.event_datetime_label
import dreifusnotes.modules.features.events.generated.resources.event_delete_button
import dreifusnotes.modules.features.events.generated.resources.event_edit_title
import dreifusnotes.modules.features.events.generated.resources.event_new_title
import dreifusnotes.modules.features.events.generated.resources.event_picker_ok
import dreifusnotes.modules.features.events.generated.resources.event_save_button
import dreifusnotes.modules.features.events.generated.resources.event_time_label
import dreifusnotes.modules.features.events.generated.resources.event_title_label
import dreifusnotes.modules.features.events.generated.resources.event_title_placeholder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

class EditEventScreen(
    private val eventId: Long? = null,
) : BottomSheetScreen {

    @Composable
    override fun Content() {
        val vm = metroViewModel<EditEventViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val bottomSheetNav = Navigation.bottomSheet

        LaunchedEffect(Unit) {
            vm.dispatch(EditEventEvent.Ui.Init(eventId))
        }
        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    EditEventEffect.Close -> bottomSheetNav.pop()
                }
            }
        }

        EditEventContent(
            state = state,
            onEvent = vm::dispatch,
        )
    }
}

@Composable
private fun EditEventContent(
    state: EditEventState,
    onEvent: (EditEventEvent.Ui) -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = stringResource(
                    if (state.isNew) Res.string.event_new_title else Res.string.event_edit_title
                ),
                style = AppTheme.typography.heading4,
                color = AppTheme.colors.contentPrimary,
            )

            Column {
                AppSectionLabel(stringResource(Res.string.event_title_label))
                AppTextField(
                    value = state.title,
                    onValueChange = { onEvent(EditEventEvent.Ui.TitleChanged(it)) },
                    labelText = stringResource(Res.string.event_title_placeholder),
                )
            }

            Column {
                AppSectionLabel(stringResource(Res.string.event_datetime_label))
                AppGroup {
                    AppRow(
                        label = stringResource(Res.string.event_date_label),
                        onClick = { onEvent(EditEventEvent.Ui.DateClick) },
                        trailing = { RowValue(EventDateFormat.dateRowLabel(state.at)) },
                    )
                    AppRow(
                        label = stringResource(Res.string.event_time_label),
                        showDivider = false,
                        onClick = { onEvent(EditEventEvent.Ui.TimeClick) },
                        trailing = { RowValue(EventDateFormat.timeLabel(state.at)) },
                    )
                }
            }

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(
                    if (state.isNew) Res.string.event_create_button else Res.string.event_save_button
                ),
                status = when {
                    state.isSaving -> ButtonStatus.Loading
                    state.title.isBlank() -> ButtonStatus.Disabled
                    else -> ButtonStatus.Enabled
                },
                onClick = { onEvent(EditEventEvent.Ui.SaveClick) },
            )

            if (!state.isNew) {
                Text(
                    text = stringResource(Res.string.event_delete_button),
                    style = AppTheme.typography.headlineMedium,
                    color = AppTheme.colors.accentError,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { onEvent(EditEventEvent.Ui.DeleteClick) },
                        ),
                )
            }
        }
    }

    if (state.showDatePicker) {
        EventDatePickerDialog(at = state.at, onEvent = onEvent)
    }
    if (state.showTimePicker) {
        EventTimePickerDialog(at = state.at, onEvent = onEvent)
    }
}

@Composable
private fun RowValue(text: String) {
    Text(
        text = text,
        style = AppTheme.typography.bodySmall,
        color = AppTheme.colors.contentSecondary,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventDatePickerDialog(
    at: Long,
    onEvent: (EditEventEvent.Ui) -> Unit,
) {
    // The material date picker works with UTC-midnight millis, not local-zone moments.
    val zone = TimeZone.currentSystemDefault()
    val initialDate = Instant.fromEpochMilliseconds(at).toLocalDateTime(zone).date
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
    )
    DatePickerDialog(
        onDismissRequest = { onEvent(EditEventEvent.Ui.PickerDismissed) },
        confirmButton = {
            TextButton(onClick = {
                pickerState.selectedDateMillis?.let { onEvent(EditEventEvent.Ui.DatePicked(it)) }
                    ?: onEvent(EditEventEvent.Ui.PickerDismissed)
            }) {
                Text(stringResource(Res.string.event_picker_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(EditEventEvent.Ui.PickerDismissed) }) {
                Text(stringResource(Res.string.event_cancel))
            }
        },
    ) {
        DatePicker(state = pickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventTimePickerDialog(
    at: Long,
    onEvent: (EditEventEvent.Ui) -> Unit,
) {
    val time = Instant.fromEpochMilliseconds(at)
        .toLocalDateTime(TimeZone.currentSystemDefault()).time
    val pickerState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute,
        is24Hour = false,
    )
    AlertDialog(
        onDismissRequest = { onEvent(EditEventEvent.Ui.PickerDismissed) },
        text = { TimePicker(state = pickerState) },
        confirmButton = {
            TextButton(onClick = {
                onEvent(EditEventEvent.Ui.TimePicked(pickerState.hour, pickerState.minute))
            }) {
                Text(stringResource(Res.string.event_picker_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(EditEventEvent.Ui.PickerDismissed) }) {
                Text(stringResource(Res.string.event_cancel))
            }
        },
        containerColor = AppTheme.colors.backgroundBase,
        shape = AppTheme.shapes.dialog,
    )
}

@Preview
@Composable
private fun PreviewEditEvent() {
    AppPreview {
        EditEventContent(
            state = EditEventState(title = "Dentist appointment", at = 1_760_083_200_000),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewEditEventExisting() {
    AppPreview {
        EditEventContent(
            state = EditEventState(eventId = 1, title = "Dentist appointment", at = 1_760_083_200_000),
            onEvent = {},
        )
    }
}
