// Asked gemini to comment all the important modules forfuture debugging
package rick.loves.lololol.NotesUi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import rick.loves.lololol.Data.Note
import rick.loves.lololol.Data.Security
import rick.loves.lololol.Data.getGradientForId
import java.io.File

/**
 * This file contains various reusable UI components used throughout the Notes application.
 * These components range from simple UI elements like cards and buttons to more complex,
 * stateful components like the search bar and PIN entry dialogs.
 * Commented by Gemini to ease future debugging and maintenance.
 */

/**
 * A top app bar that displays the app title and handles a search functionality.
 * When the search icon is clicked, it transforms into a search input field.
 *
 * @param searchQuery The current text in the search input field.
 * @param onSearchQueryChange Callback that is invoked when the search query changes.
 * @param onAboutClick Callback that is invoked when the 'About' icon is clicked.
 * @param onSearchClosed Callback that is invoked when the search bar is closed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAboutClick: () -> Unit,
    onSearchClosed: () -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Automatically focus the search field and show the keyboard when search becomes active.
    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    TopAppBar(
        title = {
            if (isSearchActive) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeholder = { Text("Search notes...") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
unfocusedContainerColor = Color.Transparent
                    )
                )
            } else {
                Text("Notes")
            }
        },
        actions = {
            if (isSearchActive) {
                IconButton(onClick = {
                    if (searchQuery.isNotEmpty()) {
                        onSearchQueryChange("")
                    } else {
                        isSearchActive = false
                        onSearchClosed()
                    }
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Close Search")
                }
            } else {
                IconButton(onClick = { isSearchActive = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = onAboutClick) {
                    Icon(Icons.Default.Info, contentDescription = "About")
                }
            }
        },
        windowInsets = WindowInsets.statusBars
    )
}

/**
 * Displays a single note item in a card with a gradient background.
 * The gradient is deterministically chosen based on the note's ID.
 * Handles the UI for a locked note by blurring the title and showing a centered placeholder instead of the content.
 *
 * @param note The [Note] object to display.
 * @param onClick Callback that is invoked when the card is clicked.
 */
@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .heightIn(max = 200.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        val (startColor, endColor) = getGradientForId(note.id)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(startColor), Color(endColor))
                    )
                )
        ) {
            // This Column holds the content visible at the top (title) and the unlocked content.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .padding(end = 4.dp)
                            .then(if (note.isLocked) Modifier.blur(4.dp) else Modifier)
                    )
                    if (note.isLocked) {
                        Icon(Icons.Default.Lock, contentDescription = "Locked")
                    }
                }

                if (!note.isLocked) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = note.content.take(30) + if (note.content.length > 30) "..." else "",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 5
                    )
                }
            }

            // This Box is only visible for locked notes and is used to center the placeholder text.
            if (note.isLocked) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Access is Restricted",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * A full-screen composable shown when the user has no notes.
 * It displays an icon with a subtle scaling animation and a helpful message
 * to prompt the user to create their first note.
 */
@Composable
fun AnimatedEmptyState() {
    val infiniteTransition = rememberInfiniteTransition(label = "empty_state_transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "empty_state_scale"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add a note",
            modifier = Modifier
                .size(128.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No notes yet. Tap the '+' button to add one.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * A full-screen composable shown when a search yields no results.
 * It displays an icon and a message indicating that the search was empty.
 */
@Composable
fun NoSearchResults() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.SearchOff,
            contentDescription = "No results found",
            modifier = Modifier
                .size(128.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No results found for your query.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * A component with a title and a [Switch] for enabling or disabling the lock on a note.
 * It handles the logic of checking if a PIN is already set before allowing the toggle.
 * If no PIN is set, it invokes [onPinRequired] to prompt the user to create one.
 *
 * @param isLocked The current locked state of the note.
 * @param onToggle Callback to change the locked state.
 * @param filesDir The application's file directory, used for PIN storage operations.
 * @param onPinRequired Callback to be invoked when a PIN needs to be created.
 */
@Composable
fun LockToggle(
    isLocked: Boolean, 
    onToggle: (Boolean) -> Unit,
    filesDir: File,
    onPinRequired: () -> Unit
){
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Lock this note")
            Switch(
                checked = isLocked,
                onCheckedChange = { 
                    if (Security.isPinSet(filesDir)) {
                        onToggle(it)
                    } else {
                        onPinRequired()
                    }
                }
            )
        }

        AnimatedVisibility(visible = isLocked) {
            Text(
                text = "This note will require a PIN to access.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * A full-screen UI for users to enter their PIN.
 * It validates the entered PIN against the one stored on the device.
 * This is likely used to unlock a note or the entire app.
 *
 * @param onPinVerified Callback that is invoked when the correct PIN is entered.
 * @param filesDir The application's file directory, used for PIN verification.
 */
@Composable
fun PinEntryScreen(
    onPinVerified: () -> Unit,
    filesDir: File
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter PIN", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = pin,
            onValueChange = { if (it.length <= 4) pin = it },
            label = { Text("PIN") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        )
        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (Security.verifyPin(filesDir, pin)) {
                onPinVerified()
            } else {
                error = "Incorrect PIN"
            }
        }) {
            Text("Unlock")
        }
    }
}

/**
 * An [AlertDialog] for creating a new 4-digit PIN.
 * It includes input fields for the new PIN and a confirmation PIN,
 * performs validation to ensure they match and are 4 digits long,
 * and saves the PIN securely.
 *
 * @param onDismiss Callback that is invoked when the dialog is dismissed.
 * @param onPinSet Callback that is invoked when a PIN is successfully created and saved.
 * @param filesDir The application's file directory, used for saving the PIN.
 */
@Composable
fun CreatePinDialog(
    onDismiss: () -> Unit,
    onPinSet: () -> Unit,
    filesDir: File
) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create a PIN") },
        text = {
            Column {
                Text("Create a 4-digit PIN to lock your notes.")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    label = { Text("New PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPin,
                    onValueChange = { if (it.length <= 4) confirmPin = it },
                    label = { Text("Confirm PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
                if (error.isNotEmpty()) {
                    Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (pin.length == 4 && pin == confirmPin) {
                    Security.savePin(filesDir, pin)
                    onPinSet()
                } else {
                    error = "PINs do not match or are not 4 digits long."
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
