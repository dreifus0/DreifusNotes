package com.dreifus.app.features.notes.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.notes.detail.mvu.NoteBlockUiItem
import com.dreifus.app.features.notes.detail.mvu.NoteDetailEffect
import com.dreifus.app.features.notes.detail.mvu.NoteDetailEvent
import com.dreifus.app.features.notes.detail.mvu.NoteDetailState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.template.uikit.icon.ArrowLeft24
import com.dreifus.template.uikit.glass.glassBorder
import com.dreifus.template.uikit.icon.GlassIcon
import com.dreifus.template.uikit.icon.Lock24
import com.dreifus.template.uikit.icon.MoreHoriz24
import com.dreifus.template.uikit.icon.Plus24
import com.dreifus.template.uikit.icon.Send24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.NoteCardColor
import com.dreifus.template.uikit.textField.AppTextField
import com.dreifus.template.uikit.toolbar.AppToolbar
import dev.zacsweers.metrox.viewmodel.metroViewModel

class NoteDetailScreen(
    private val noteId: Long,
    private val unlockedPin: String? = null,
) : RegularScreen {

    @Composable
    override fun Content() {
        val vm = metroViewModel<NoteDetailViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val regularNav = Navigation.regular
        val bottomSheetNav = Navigation.bottomSheet

        val imagePickerLauncher = rememberImagePicker { uri ->
            if (uri != null) vm.dispatch(NoteDetailEvent.Ui.PhotoSelected(uri))
        }

        LaunchedEffect(Unit) {
            vm.dispatch(NoteDetailEvent.Ui.Init(noteId, unlockedPin))
        }
        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    NoteDetailEffect.NavigateBack -> regularNav.pop()
                    is NoteDetailEffect.NavigateToPinSetup -> vm.pinNavigation.openPinSetup(effect.noteId)
                    NoteDetailEffect.ShowImagePicker -> imagePickerLauncher()
                    NoteDetailEffect.ShowChecklistSheet -> bottomSheetNav.navigate(
                        CreateChecklistBottomSheet { title, items ->
                            vm.dispatch(NoteDetailEvent.Ui.ChecklistConfirmed(title, items))
                        }
                    )
                }
            }
        }

        NoteDetailContent(state = state, onEvent = vm::dispatch)
    }
}

@Composable
private fun NoteDetailContent(
    state: NoteDetailState,
    onEvent: (NoteDetailEvent.Ui) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppToolbar(
            title = state.title,
            centerTitle = false,
            startBlock = {
                GlassIcon(
                    icon = AppIcons.ArrowLeft24,
                    onClick = { onEvent(NoteDetailEvent.Ui.BackClick) },
                )
            },
            endBlock = {
                GlassIcon(
                    icon = AppIcons.Lock24,
                    onClick = { onEvent(NoteDetailEvent.Ui.LockClick) },
                )
                Spacer(Modifier.width(6.dp))
                GlassIcon(
                    icon = AppIcons.MoreHoriz24,
                    onClick = { },
                )
            },
        )

        val palette = state.color.palette()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp)
                .height(3.dp)
                .background(palette.background, RoundedCornerShape(12.dp)),
        )

        val listState = rememberLazyListState()
        val lastBlockId = state.blocks.lastOrNull()?.id
        LaunchedEffect(lastBlockId) {
            val total = listState.layoutInfo.totalItemsCount
            if (total > 0) listState.animateScrollToItem(total - 1)
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(state.blocks, key = { it.id }) { block ->
                BlockItemWithHeader(
                    block = block,
                    onDelete = { onEvent(NoteDetailEvent.Ui.DeleteBlockClick(block.id)) },
                )
            }
        }

        BlockInputBar(
            text = state.inputText,
            onTextChange = { onEvent(NoteDetailEvent.Ui.InputChanged(it)) },
            onSend = { onEvent(NoteDetailEvent.Ui.SendClick) },
            onPhotoClick = { onEvent(NoteDetailEvent.Ui.PhotoClick) },
            onChecklistClick = { onEvent(NoteDetailEvent.Ui.ChecklistClick) },
        )
    }
}

@Composable
private fun BlockItemWithHeader(
    block: NoteBlockUiItem,
    onDelete: () -> Unit,
) {
    Column {
        if (block.dayHeader != null) {
            Text(
                text = block.dayHeader.orEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                style = AppTheme.typography.headlineSmall,
                color = AppTheme.colors.contentTertiary,
                textAlign = TextAlign.Center,
            )
        }
        BlockShell(time = block.time, onDelete = onDelete) {
            when (block) {
                is NoteBlockUiItem.Text -> TextBlockContent(block)
                is NoteBlockUiItem.Photo -> PhotoBlockContent()
                is NoteBlockUiItem.Checklist -> ChecklistBlockContent(block)
            }
        }
    }
}

private val blockShape = RoundedCornerShape(
    topStart = 14.dp, topEnd = 14.dp, bottomEnd = 14.dp, bottomStart = 4.dp,
)

@Composable
private fun BlockShell(
    time: String,
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth()) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { showMenu = true },
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .background(AppTheme.colors.bgCardPrimary, blockShape)
                        .glassBorder(shape = blockShape)
                        .padding(16.dp),
                ) {
                    content()
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    text = time,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.contentTertiary,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = { showMenu = false; onDelete() },
                )
            }
        }
    }
}

@Composable
private fun TextBlockContent(block: NoteBlockUiItem.Text) {
    Text(
        text = block.text,
        style = AppTheme.typography.bodyLarge,
        color = AppTheme.colors.contentPrimary,
    )
}

@Composable
private fun PhotoBlockContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                AppTheme.colors.contentTertiary.copy(alpha = 0.12f),
                RoundedCornerShape(8.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Photo",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.contentTertiary,
        )
    }
}

@Composable
private fun ChecklistBlockContent(block: NoteBlockUiItem.Checklist) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (block.title.isNotBlank()) {
            Text(
                text = block.title,
                style = AppTheme.typography.headlineSmall,
                color = AppTheme.colors.contentPrimary,
            )
        }
        block.items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                CheckboxDot()
                Text(
                    text = item,
                    style = AppTheme.typography.bodyLarge,
                    color = AppTheme.colors.contentPrimary,
                )
            }
        }
    }
}

@Composable
private fun BlockInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onPhotoClick: () -> Unit,
    onChecklistClick: () -> Unit,
) {
    var showPlusMenu by remember { mutableStateOf(false) }
    val dividerColor = AppTheme.colors.contentDividers
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = dividerColor, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundBase)
                .padding(horizontal = 12.dp)
                .padding(top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box {
                GlassIcon(
                    icon = AppIcons.Plus24,
                    onClick = { showPlusMenu = true },
                )
                DropdownMenu(
                    expanded = showPlusMenu,
                    onDismissRequest = { showPlusMenu = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Photo") },
                        onClick = { showPlusMenu = false; onPhotoClick() },
                    )
                    DropdownMenuItem(
                        text = { Text("Checklist") },
                        onClick = { showPlusMenu = false; onChecklistClick() },
                    )
                }
            }
            AppTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                labelText = "Add to note…",
                imeAction = ImeAction.Send,
                keyboardActions = KeyboardActions(onSend = { onSend() }),
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.accentPrimary, CircleShape)
                    .clickable(onClick = onSend),
                contentAlignment = Alignment.Center,
            ) {
                AppIcons.Send24(tint = AppTheme.colors.accentOnPrimary)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewNoteDetail() {
    AppPreview {
        NoteDetailContent(
            state = NoteDetailState(
                title = "Project ideas",
                color = NoteCardColor.Purple,
                blocks = listOf(
                    NoteBlockUiItem.Text(
                        1,
                        "Build a notes app with pin protection, maybe add tags later.",
                        "09:12",
                        "TUESDAY",
                    ),
                    NoteBlockUiItem.Checklist(
                        id = 2,
                        title = "Core features",
                        items = listOf("List of notes", "Create note", "Delete note"),
                        time = "09:15",
                        dayHeader = null,
                    ),
                    NoteBlockUiItem.Photo(
                        3,
                        "content://media/photo.jpg",
                        "09:32",
                        "TODAY",
                    ),
                ),
                inputText = "",
                isLoading = false,
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewNoteDetailWithInput() {
    AppPreview {
        NoteDetailContent(
            state = NoteDetailState(
                title = "Grocery list",
                color = NoteCardColor.Green,
                blocks = emptyList(),
                inputText = "Milk, bread, coffee",
                isLoading = false,
            ),
            onEvent = {},
        )
    }
}
