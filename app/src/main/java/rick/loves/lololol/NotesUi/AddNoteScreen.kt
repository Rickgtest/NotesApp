package rick.loves.lololol.NotesUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    onSaveNote: (Note) -> Unit,
    onBack: () -> Unit,
    filesDir: File
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isLocked by remember { mutableStateOf(false) }
    var showCreatePinDialog by remember { mutableStateOf(false) }

    if (showCreatePinDialog) {
        CreatePinDialog(
            onDismiss = { showCreatePinDialog = false },
            onPinSet = { 
                isLocked = true
                showCreatePinDialog = false
            },
            filesDir = filesDir
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val note = Note(title = title, content = content, isLocked = isLocked)
                        onSaveNote(note)
                    }) {
                        Icon(Icons.Default.Done, contentDescription = "Save Note")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            NoteInputView(
                title = title,
                content = content,
                isLocked = isLocked,
                onTitleChange = { title = it },
                onContentChange = { content = it },
                onLockToggle = { 
                    if (it) {
                        showCreatePinDialog = true
                    } else {
                        isLocked = false
                    }
                 },
                filesDir = filesDir
            )
        }
    }
}

@Composable
private fun NoteInputView(
    title: String,
    content: String,
    isLocked: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onLockToggle: (Boolean) -> Unit,
    filesDir: File
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )
        LockToggle(
            isLocked = isLocked, 
            onToggle = onLockToggle, 
            filesDir = filesDir, 
            onPinRequired = { onLockToggle(true) }
        )
    }
}
