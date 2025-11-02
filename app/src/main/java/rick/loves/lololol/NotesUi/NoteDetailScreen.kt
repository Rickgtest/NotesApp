// Asked gemini to comment all the important modules forfuture debugging
package rick.loves.lololol.NotesUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import rick.loves.lololol.Data.Note
import java.io.File

/**
 * A full-screen composable that displays the details of a single note.
 * It manages the locked/unlocked state of the note and allows the user to switch
 * between a read-only view and an editing view.
 *
 * @param note The [Note] to display and potentially edit.
 * @param onBack Callback invoked when the user presses the back button.
 * @param onDelete Callback invoked when the user presses the delete button.
 * @param onSave Callback invoked when the user saves changes to the note.
 * @param filesDir The application's file directory, used for PIN verification.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    onSave: (Note) -> Unit,
    filesDir: File
) {
    // Internal state for managing the UI (editing mode, content changes, unlocked status).
    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var unlocked by remember { mutableStateOf(!note.isLocked) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Note" else "Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Action buttons are only shown if the note is unlocked.
                    if (unlocked) {
                        if (isEditing) {
                            // In editing mode, show a 'Save' button.
                            IconButton(onClick = {
                                val updatedNote = note.copy(title = title, content = content)
                                onSave(updatedNote)
                                isEditing = false // Exit editing mode after saving.
                            }) {
                                Icon(Icons.Default.Save, contentDescription = "Save Note")
                            }
                        } else {
                            // In read-only mode, show an 'Edit' button.
                            IconButton(onClick = { isEditing = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Note")
                            }
                        }
                        // The 'Delete' button is always available when unlocked.
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Note")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (unlocked) {
                // If unlocked, show either the editing or reading view.
                if (isEditing) {
                    NoteEditView(
                        title = title,
                        content = content,
                        onTitleChange = { title = it },
                        onContentChange = { content = it }
                    )
                } else {
                    NoteReadView(title = title, content = content)
                }
            } else {
                // If locked, show the PIN entry screen.
                // The `unlocked` state will be updated upon successful verification.
                PinEntryScreen(onPinVerified = { unlocked = true }, filesDir = filesDir)
            }
        }
    }
}

/**
 * A simple, read-only view of a note's title and content.
 *
 * @param title The title of the note.
 * @param content The full content of the note.
 */
@Composable
fun NoteReadView(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * An editable view of a note's title and content, using [OutlinedTextField]s.
 * State is hoisted to the parent composable.
 *
 * @param title The current title to display in the text field.
 * @param content The current content to display in the text field.
 * @param onTitleChange Callback invoked when the title text changes.
 * @param onContentChange Callback invoked when the content text changes.
 */
@Composable
fun NoteEditView(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )
        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            label = { Text("Content") },
            modifier = Modifier.fillMaxSize(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )
    }
}
