// Asked gemini to comment all the important modules forfuture debugging
package rick.loves.lololol.NotesUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rick.loves.lololol.Data.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    notes: List<Note>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddNote: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            NotesTopBar (
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onAboutClick = onAboutClick,
                onSearchClosed = { onSearchQueryChange("") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        if (notes.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notes) { note ->
                    NoteCard(note = note, onClick = { onNoteClick(note.id) })
                }
            }
        } else {
            if (searchQuery.isNotEmpty()) {
                NoSearchResults()
            } else {
                AnimatedEmptyState()
            }
        }
    }
}

@Preview
@Composable
fun NoteScreenPreview() {
    val notes = listOf(
        Note(id = 1, title = "Note 1", content = "Content 1"),
        Note(id = 2, title = "Note 2", content = "Content 2", isLocked = true),
        Note(id = 3, title = "Note 3", content = "Content 3"),
        Note(id = 4, title = "Note 4", content = "Content 4"),
        Note(id = 5, title = "Note 5", content = "Content 5"),
        Note(id = 6, title = "Note 6", content = "Content 6")
    )
    NoteScreen(
        notes = notes,
        searchQuery = "",
        onSearchQueryChange = {},
        onAddNote = {},
        onNoteClick = {},
        onAboutClick = {}
    )
}
