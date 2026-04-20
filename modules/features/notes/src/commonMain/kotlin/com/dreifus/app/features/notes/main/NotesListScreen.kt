package com.dreifus.app.features.notes.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.notes.create.CreateNoteScreen
import com.dreifus.app.features.notes.detail.NoteDetailScreen
import com.dreifus.app.features.notes.main.mvu.NoteUiItem
import com.dreifus.app.features.notes.main.mvu.NotesListEffect
import com.dreifus.app.features.notes.main.mvu.NotesListEvent
import com.dreifus.app.features.notes.main.mvu.NotesListState
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.ui.RootScreenWithTabs
import com.dreifus.template.uikit.button.AppButton
import com.dreifus.template.uikit.card.AppCard
import com.dreifus.template.uikit.icon.GlassIcon
import com.dreifus.template.uikit.icon.Plus24
import com.dreifus.template.uikit.icon.Search24
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcons
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.NoteCardColor
import com.dreifus.template.uikit.textField.AppTextField
import dev.zacsweers.metrox.viewmodel.metroViewModel

class NotesListScreen : RootScreenWithTabs {

    @Composable
    override fun Content() {
        val vm = metroViewModel<NotesListViewModel>()
        val state by vm.state.collectAsStateWithLifecycle()
        val bottomSheetNav = Navigation.bottomSheet
        val regularNav = Navigation.regular

        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    is NotesListEffect.NavigateToNewNote -> bottomSheetNav.navigate(CreateNoteScreen())
                    is NotesListEffect.NavigateToNote -> {
                        if (effect.isProtected) vm.pinNavigation.openPinLock(effect.id) { id, pin ->
                            regularNav.replaceLast(NoteDetailScreen(id, pin))
                        } else regularNav.navigate(NoteDetailScreen(effect.id))
                    }
                }
            }
        }

        NotesListContent(
            state = state,
            onEvent = vm::dispatch,
        )
    }
}

@Composable
private fun NotesListContent(
    state: NotesListState,
    onEvent: (NotesListEvent.Ui) -> Unit,
) {
    val isEmpty = !state.isLoading && state.notes.isEmpty() && state.query.isEmpty()

    if (isEmpty) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            NotesListHeader(onAddClick = { onEvent(NotesListEvent.Ui.AddClick) })
            NotesEmptyState(onCreateClick = { onEvent(NotesListEvent.Ui.AddClick) })
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { NotesListHeader(onAddClick = { onEvent(NotesListEvent.Ui.AddClick) }) }

        item {
            AppTextField(
                value = state.query,
                onValueChange = { onEvent(NotesListEvent.Ui.QueryChanged(it)) },
                labelText = "Search notes",
                leadingIcon = AppIcons.Search24,
                imeAction = ImeAction.Search,
            )
        }

        items(state.notes, key = { it.id }) { note ->
            AppCard(
                title = note.title,
                body = note.body,
                date = note.date,
                color = note.color,
                isLocked = note.isProtected,
                onClick = { onEvent(NotesListEvent.Ui.NoteClick(note.id)) },
            )
        }
    }
}

@Composable
private fun NotesEmptyState(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        StackedCardsIllustration()

        Spacer(Modifier.height(28.dp))

        Text(
            text = "No notes yet",
            style = AppTheme.typography.heading4,
            color = AppTheme.colors.contentPrimary,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Jot down an idea, a list, a moment.\nTap the + to create your first note.",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.contentSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(24.dp))

        AppButton(
            text = "Create a note",
            onClick = onCreateClick,
        )

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun StackedCardsIllustration() {
    val greenBg = NoteCardColor.Green.palette().background
    val pinkBg = NoteCardColor.Pink.palette().background
    val purpleBg = NoteCardColor.Purple.palette().background
    val purpleTitle = NoteCardColor.Purple.palette().title
    val purpleBody = NoteCardColor.Purple.palette().body

    Box(
        modifier = Modifier.size(width = 140.dp, height = 110.dp),
    ) {
        // Back card — green, rotated -8°
        Box(
            modifier = Modifier
                .offset(x = 20.dp, y = 10.dp)
                .size(width = 110.dp, height = 80.dp)
                .rotate(-8f)
                .background(greenBg, RoundedCornerShape(14.dp)),
        )
        // Middle card — pink, rotated +3°
        Box(
            modifier = Modifier
                .offset(x = 14.dp, y = 6.dp)
                .size(width = 110.dp, height = 80.dp)
                .rotate(3f)
                .background(pinkBg, RoundedCornerShape(14.dp)),
        )
        // Front card — purple, rotated -2°, with placeholder lines
        Box(
            modifier = Modifier
                .offset(x = 8.dp, y = 0.dp)
                .size(width = 110.dp, height = 80.dp)
                .rotate(-2f)
                .background(purpleBg, RoundedCornerShape(14.dp))
                .padding(12.dp),
        ) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(6.dp)
                        .background(purpleTitle.copy(alpha = 0.6f), RoundedCornerShape(3.dp)),
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(4.dp)
                        .background(purpleBody.copy(alpha = 0.5f), RoundedCornerShape(2.dp)),
                )
            }
        }
    }
}

@Composable
private fun NotesListHeader(onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Notes",
            style = AppTheme.typography.heading3,
            color = AppTheme.colors.contentPrimary,
        )
        GlassIcon(AppIcons.Plus24, onClick = onAddClick)

    }
}

@Preview
@Composable
private fun PreviewNotesList() {
    AppPreview {
        NotesListContent(
            state = NotesListState(notes = previewNotes, isLoading = false),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun PreviewNotesEmpty() {
    AppPreview {
        NotesListContent(
            state = NotesListState(notes = emptyList(), isLoading = false),
            onEvent = {},
        )
    }
}

private val previewNotes = listOf(
    NoteUiItem(
        1,
        "Project ideas",
        "Build a notes app with pin protection, maybe add tags later…",
        "TODAY",
        NoteCardColor.Purple,
        false
    ),
    NoteUiItem(
        2,
        "Protected note",
        "Lorem ipsum dolor sit amet",
        "YESTERDAY",
        NoteCardColor.Pink,
        true
    ),
    NoteUiItem(
        3,
        "Grocery list",
        "Milk, bread, coffee beans, avocados, olive oil, tomatoes",
        "MON",
        NoteCardColor.Green,
        false
    ),
    NoteUiItem(
        4,
        "Book notes",
        "Chapter 3 — key insight about habit formation cycles",
        "APR 12",
        NoteCardColor.Orange,
        false
    ),
)
