// Asked gemini to comment all the important modules for future debugging
package rick.loves.lololol.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import rick.loves.lololol.NotesUi.AboutScreen
import rick.loves.lololol.NotesUi.AddNoteScreen
import rick.loves.lololol.NotesUi.NoteDetailScreen
import rick.loves.lololol.NotesUi.NoteScreen
import rick.loves.lololol.Data.Note
import java.io.File


sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object AddNote : Screen("add_note")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object About : Screen("about")
    // object Settings : Screen("settings") // might need for future
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    notes: List<Note>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onAddNote: (Note) -> Unit,
    onUpdateNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    filesDir: File
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route,
        modifier = modifier
    ) {
        composable(Screen.Notes.route) {
            NoteScreen(
                notes = notes,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onAddNote = { navController.navigate(Screen.AddNote.route) },
                onNoteClick = { noteId: Int ->
                    navController.navigate(
                        Screen.NoteDetail.createRoute(
                            noteId
                        )
                    )
                },
                onAboutClick = { navController.navigate(Screen.About.route) },
                // onSettingsClick = { navController.navigate(Screen.Settings.route) } // might need for future
            )
        }
        composable(Screen.AddNote.route) {
            AddNoteScreen(
                onSaveNote = { note: Note ->
                    onAddNote(note)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
                filesDir = filesDir
            )
        }
        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId")
            val note = notes.find { it.id == noteId }

            if (note != null) {
                NoteDetailScreen(
                    note = note,
                    onBack = { navController.popBackStack() },
                    onDelete = {
                        onDeleteNote(note)
                        navController.popBackStack()
                    },
                    onSave = { updatedNote: Note ->
                        onUpdateNote(updatedNote)
                        navController.popBackStack()
                    },
                    filesDir = filesDir
                )
            } else {
                Text(text = "Note not found")
            }
        }
        composable(Screen.About.route) {
            AboutScreen()
        }
        /* // might need for future
        composable(Screen.Settings.route) {
            Text(text = "Settings coming soon")
        }
        */
    }
}