// Asked gemini to comment all the important modules for future debugging
package rick.loves.lololol.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import rick.loves.lololol.Data.NoteDatabase
import rick.loves.lololol.Data.NotesRepository
import rick.loves.lololol.Viewmodel.NotesViewModelFactory
import rick.loves.lololol.navigation.AppNavHost
import rick.loves.lololol.ui.theme.LolololTheme
import rick.loves.lololol.Viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = NoteDatabase.getDatabase(this)
        val repository = NotesRepository(database.noteDao())
        val factory = NotesViewModelFactory(repository)
        val viewModel: NotesViewModel by viewModels { factory }

        setContent {
            LolololTheme {
                val navController = rememberNavController()
                val notes by viewModel.allNotes.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()

                AppNavHost(
                    navController = navController,
                    notes = notes,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                    onAddNote = { viewModel.insert(it) },
                    onUpdateNote = { viewModel.update(it) },
                    onDeleteNote = { viewModel.delete(it) },
                    filesDir = filesDir
                )
            }
        }
    }
}