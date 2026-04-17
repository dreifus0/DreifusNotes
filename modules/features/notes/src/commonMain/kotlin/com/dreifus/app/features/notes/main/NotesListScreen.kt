package com.dreifus.app.features.notes.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreifus.app.features.notes.main.mvu.NoteUiItem
import com.dreifus.app.features.notes.main.mvu.NotesListEffect
import com.dreifus.app.features.notes.main.mvu.NotesListEvent
import com.dreifus.app.features.notes.main.mvu.NotesListState
import com.dreifus.navigation.ui.RootScreenWithTabs
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

        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    is NotesListEffect.NavigateToNewNote -> Unit
                    is NotesListEffect.NavigateToNote -> Unit
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
