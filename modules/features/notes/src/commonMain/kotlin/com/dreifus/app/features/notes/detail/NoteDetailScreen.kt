package com.dreifus.app.features.notes.detail

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt
import com.dreifus.template.uikit.icon.Checklist24
import com.dreifus.template.uikit.icon.Download24
import com.dreifus.template.uikit.icon.Edit24
import com.dreifus.template.uikit.icon.Palette24
import com.dreifus.template.uikit.icon.Trash24
import com.dreifus.template.uikit.menu.ContextMenuPopup
import com.dreifus.template.uikit.menu.ContextMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.positionInWindow
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
import dreifusnotes.modules.features.notes.generated.resources.Res
import dreifusnotes.modules.features.notes.generated.resources.note_block_copy
import dreifusnotes.modules.features.notes.generated.resources.note_block_edit
import dreifusnotes.modules.features.notes.generated.resources.note_block_edit_cancel
import dreifusnotes.modules.features.notes.generated.resources.note_block_edit_save
import dreifusnotes.modules.features.notes.generated.resources.note_block_edit_title
import dreifusnotes.modules.features.notes.generated.resources.note_detail_add_checklist
import dreifusnotes.modules.features.notes.generated.resources.note_detail_add_photo
import dreifusnotes.modules.features.notes.generated.resources.note_detail_delete
import dreifusnotes.modules.features.notes.generated.resources.note_detail_input_placeholder
import dreifusnotes.modules.features.notes.generated.resources.note_detail_photo_label
import dreifusnotes.modules.features.notes.generated.resources.note_menu_change_color
import dreifusnotes.modules.features.notes.generated.resources.note_menu_change_color_title
import dreifusnotes.modules.features.notes.generated.resources.note_menu_delete
import dreifusnotes.modules.features.notes.generated.resources.note_menu_delete_confirm
import dreifusnotes.modules.features.notes.generated.resources.note_menu_delete_message
import dreifusnotes.modules.features.notes.generated.resources.note_menu_delete_title
import dreifusnotes.modules.features.notes.generated.resources.note_menu_rename
import dreifusnotes.modules.features.notes.generated.resources.note_menu_rename_label
import dreifusnotes.modules.features.notes.generated.resources.note_menu_rename_title
import dreifusnotes.modules.features.notes.generated.resources.note_menu_share
import org.jetbrains.compose.resources.stringResource

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
        val clipboardManager = LocalClipboardManager.current
        val shareLauncher = rememberShareLauncher()

        LaunchedEffect(Unit) {
            vm.dispatch(NoteDetailEvent.Ui.Init(noteId, unlockedPin))
        }
        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    NoteDetailEffect.NavigateBack -> regularNav.pop()
                    is NoteDetailEffect.NavigateToPinSetup -> vm.pinNavigation.openPinSetup(effect.noteId) { id, pin ->
                        regularNav.replaceLast(NoteDetailScreen(id, pin))
                    }
                    NoteDetailEffect.ShowImagePicker -> imagePickerLauncher()
                    NoteDetailEffect.ShowChecklistSheet -> bottomSheetNav.navigate(
                        CreateChecklistBottomSheet { title, items ->
                            vm.dispatch(NoteDetailEvent.Ui.ChecklistConfirmed(title, items))
                        }
                    )
                    is NoteDetailEffect.CopyToClipboard -> clipboardManager.setText(AnnotatedString(effect.text))
                    is NoteDetailEffect.ShareNote -> shareLauncher(effect.text)
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
    var showMoreMenu by remember { mutableStateOf(false) }
    var moreAnchorPosition by remember { mutableStateOf(IntOffset.Zero) }
    var moreAnchorSize by remember { mutableStateOf(IntSize.Zero) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameValue by remember { mutableStateOf("") }
    var showColorDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppToolbar(
            title = state.title,
            centerTitle = false,
            startBlock = {
                GlassIcon(
                    icon = AppIcons.ArrowLeft24,
                    onClick = { onEvent(NoteDetailEvent.Ui.BackClick) },
                    size = 36.dp,
                    iconSize = 16.dp,
                )
            },
            endBlock = {
                GlassIcon(
                    icon = AppIcons.Lock24,
                    onClick = { onEvent(NoteDetailEvent.Ui.LockClick) },
                    size = 36.dp,
                    iconSize = 16.dp,
                )
                Spacer(Modifier.width(6.dp))
                GlassIcon(
                    icon = AppIcons.MoreHoriz24,
                    onClick = { showMoreMenu = true },
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val pos = coords.positionInWindow()
                        moreAnchorPosition = IntOffset(pos.x.roundToInt(), pos.y.roundToInt())
                        moreAnchorSize = coords.size
                    },
                    size = 36.dp,
                    iconSize = 16.dp,
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

        if (state.isBlocksLoading) {
            BlocksShimmer(modifier = Modifier.weight(1f).fillMaxWidth())
        } else {
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
                    BlockItemWithHeader(block = block, onEvent = onEvent)
                }
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

    if (showMoreMenu) {
        ContextMenuPopup(
            anchorPosition = moreAnchorPosition,
            anchorSize = moreAnchorSize,
            alignEnd = true,
            onDismissRequest = { showMoreMenu = false },
            items = listOf(
                ContextMenuItem(
                    label = stringResource(Res.string.note_menu_rename),
                    icon = AppIcons.Edit24,
                    onClick = {
                        showMoreMenu = false
                        renameValue = state.title
                        showRenameDialog = true
                    },
                ),
                ContextMenuItem(
                    label = stringResource(Res.string.note_menu_change_color),
                    icon = AppIcons.Palette24,
                    onClick = { showMoreMenu = false; showColorDialog = true },
                ),
                ContextMenuItem(
                    label = stringResource(Res.string.note_menu_share),
                    icon = AppIcons.Send24,
                    onClick = { showMoreMenu = false; onEvent(NoteDetailEvent.Ui.ShareClick) },
                ),
                ContextMenuItem(
                    label = stringResource(Res.string.note_menu_delete),
                    icon = AppIcons.Trash24,
                    isDestructive = true,
                    onClick = { showMoreMenu = false; showDeleteDialog = true },
                ),
            ),
        )
    }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = {
                Text(
                    text = stringResource(Res.string.note_menu_rename_title),
                    style = AppTheme.typography.headlineLarge,
                    color = AppTheme.colors.contentPrimary,
                )
            },
            text = {
                AppTextField(
                    value = renameValue,
                    onValueChange = { renameValue = it },
                    labelText = stringResource(Res.string.note_menu_rename_label),
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showRenameDialog = false
                    if (renameValue.isNotBlank()) onEvent(NoteDetailEvent.Ui.RenameConfirmed(renameValue))
                }) {
                    Text(
                        text = stringResource(Res.string.note_block_edit_save),
                        color = AppTheme.colors.accentPrimary,
                        style = AppTheme.typography.headlineMedium,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text(
                        text = stringResource(Res.string.note_block_edit_cancel),
                        color = AppTheme.colors.contentSecondary,
                        style = AppTheme.typography.headlineMedium,
                    )
                }
            },
            containerColor = AppTheme.colors.backgroundBase,
            shape = AppTheme.shapes.dialog,
        )
    }

    if (showColorDialog) {
        AlertDialog(
            onDismissRequest = { showColorDialog = false },
            title = {
                Text(
                    text = stringResource(Res.string.note_menu_change_color_title),
                    style = AppTheme.typography.headlineLarge,
                    color = AppTheme.colors.contentPrimary,
                )
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    NoteCardColor.entries.forEach { color ->
                        val colorPalette = color.palette()
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(colorPalette.background, CircleShape)
                                .then(
                                    if (color == state.color) {
                                        Modifier.border(2.dp, AppTheme.colors.contentPrimary, CircleShape)
                                    } else Modifier
                                )
                                .clickable {
                                    showColorDialog = false
                                    onEvent(NoteDetailEvent.Ui.ColorChangeConfirmed(color))
                                },
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showColorDialog = false }) {
                    Text(
                        text = stringResource(Res.string.note_block_edit_cancel),
                        color = AppTheme.colors.contentSecondary,
                        style = AppTheme.typography.headlineMedium,
                    )
                }
            },
            containerColor = AppTheme.colors.backgroundBase,
            shape = AppTheme.shapes.dialog,
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = stringResource(Res.string.note_menu_delete_title),
                    style = AppTheme.typography.headlineLarge,
                    color = AppTheme.colors.contentPrimary,
                )
            },
            text = {
                Text(
                    text = stringResource(Res.string.note_menu_delete_message),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.contentSecondary,
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onEvent(NoteDetailEvent.Ui.DeleteNoteConfirmed)
                }) {
                    Text(
                        text = stringResource(Res.string.note_menu_delete_confirm),
                        color = AppTheme.colors.accentError,
                        style = AppTheme.typography.headlineMedium,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        text = stringResource(Res.string.note_block_edit_cancel),
                        color = AppTheme.colors.contentSecondary,
                        style = AppTheme.typography.headlineMedium,
                    )
                }
            },
            containerColor = AppTheme.colors.backgroundBase,
            shape = AppTheme.shapes.dialog,
        )
    }
}

@Composable
private fun BlockItemWithHeader(
    block: NoteBlockUiItem,
    onEvent: (NoteDetailEvent.Ui) -> Unit,
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
        BlockShell(
            time = block.time,
            onDelete = { onEvent(NoteDetailEvent.Ui.DeleteBlockClick(block.id)) },
            onCopy = when (block) {
                is NoteBlockUiItem.Photo -> null
                else -> { { onEvent(NoteDetailEvent.Ui.CopyBlockClick(block.id)) } }
            },
            initialEditText = (block as? NoteBlockUiItem.Text)?.text,
            onEditConfirm = if (block is NoteBlockUiItem.Text) {
                { newText -> onEvent(NoteDetailEvent.Ui.EditBlockConfirmed(block.id, newText)) }
            } else null,
        ) {
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
    onCopy: (() -> Unit)? = null,
    initialEditText: String? = null,
    onEditConfirm: ((String) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editValue by remember { mutableStateOf("") }
    var anchorPosition by remember { mutableStateOf(IntOffset.Zero) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .onGloballyPositioned { coords ->
                    val pos = coords.positionInWindow()
                    anchorPosition = IntOffset(pos.x.roundToInt(), pos.y.roundToInt())
                    anchorSize = coords.size
                }
                .clickable { showMenu = true },
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
    }

    if (showMenu) {
        ContextMenuPopup(
            anchorPosition = anchorPosition,
            anchorSize = anchorSize,
            alignEnd = true,
            onDismissRequest = { showMenu = false },
            items = buildList {
                if (onCopy != null) add(ContextMenuItem(
                    label = stringResource(Res.string.note_block_copy),
                    onClick = { showMenu = false; onCopy() },
                ))
                if (initialEditText != null) add(ContextMenuItem(
                    label = stringResource(Res.string.note_block_edit),
                    icon = AppIcons.Edit24,
                    onClick = { showMenu = false; editValue = initialEditText; showEditDialog = true },
                ))
                add(ContextMenuItem(
                    label = stringResource(Res.string.note_detail_delete),
                    icon = AppIcons.Trash24,
                    isDestructive = true,
                    onClick = { showMenu = false; onDelete() },
                ))
            },
        )
    }

    if (showEditDialog && onEditConfirm != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = stringResource(Res.string.note_block_edit_title),
                    style = AppTheme.typography.headlineLarge,
                    color = AppTheme.colors.contentPrimary,
                )
            },
            text = {
                AppTextField(
                    value = editValue,
                    onValueChange = { editValue = it },
                    labelText = "",
                )
            },
            confirmButton = {
                TextButton(onClick = { showEditDialog = false; onEditConfirm(editValue) }) {
                    Text(
                        text = stringResource(Res.string.note_block_edit_save),
                        color = AppTheme.colors.accentPrimary,
                        style = AppTheme.typography.headlineMedium,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text(
                        text = stringResource(Res.string.note_block_edit_cancel),
                        color = AppTheme.colors.contentSecondary,
                        style = AppTheme.typography.headlineMedium,
                    )
                }
            },
            containerColor = AppTheme.colors.backgroundBase,
            shape = AppTheme.shapes.dialog,
        )
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
            text = stringResource(Res.string.note_detail_photo_label),
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
    var plusPosition by remember { mutableStateOf(IntOffset.Zero) }
    var plusSize by remember { mutableStateOf(IntSize.Zero) }
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = AppTheme.colors.contentTertiary.copy(alpha = 0.4f), thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundBase)
                .padding(horizontal = 12.dp)
                .padding(top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GlassIcon(
                icon = AppIcons.Plus24,
                onClick = { showPlusMenu = true },
                modifier = Modifier.onGloballyPositioned { coords ->
                    val pos = coords.positionInWindow()
                    plusPosition = IntOffset(pos.x.roundToInt(), pos.y.roundToInt())
                    plusSize = coords.size
                },
            )
            AppTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                labelText = stringResource(Res.string.note_detail_input_placeholder),
                imeAction = ImeAction.Send,
                keyboardActions = KeyboardActions(onSend = { onSend() }),
            )
            GlassIcon(
                icon = AppIcons.Send24,
                onClick = onSend,
                iconSize = 18.dp,
                backgroundColor = AppTheme.colors.accentPrimary,
                iconTint = AppTheme.colors.accentOnPrimary,
            )
        }
    }
    if (showPlusMenu) {
        ContextMenuPopup(
            anchorPosition = plusPosition,
            anchorSize = plusSize,
            alignEnd = false,
            onDismissRequest = { showPlusMenu = false },
            items = listOf(
                ContextMenuItem(
                    label = stringResource(Res.string.note_detail_add_photo),
                    icon = AppIcons.Download24,
                    onClick = { showPlusMenu = false; onPhotoClick() },
                ),
                ContextMenuItem(
                    label = stringResource(Res.string.note_detail_add_checklist),
                    icon = AppIcons.Checklist24,
                    onClick = { showPlusMenu = false; onChecklistClick() },
                ),
            ),
        )
    }
}

@Composable
private fun BlocksShimmer(modifier: Modifier = Modifier) {
    val brush = shimmerBrush()
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        repeat(4) { index ->
            val widthFraction = when (index % 4) {
                0 -> 0.85f
                1 -> 0.60f
                2 -> 0.75f
                else -> 0.50f
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(widthFraction)
                    .height(36.dp)
                    .background(brush, blockShape),
            )
        }
    }
}

@Composable
private fun shimmerBrush(): Brush {
    val colors = listOf(
        AppTheme.colors.bgCardPrimary,
        AppTheme.colors.backgroundSecondary,
        AppTheme.colors.bgCardPrimary,
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val x by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerX",
    )
    return Brush.linearGradient(
        colors = colors,
        start = Offset(x - 400f, 0f),
        end = Offset(x, 0f),
    )
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
