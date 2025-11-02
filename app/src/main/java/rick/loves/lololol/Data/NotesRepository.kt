// Asked gemini to comment all the important modules for future debugging
package rick.loves.lololol.Data

import kotlinx.coroutines.flow.Flow

class NotesRepository(private val notesDao: NotesDao) {
    val allNotes: Flow<List<Note>> = notesDao.getAllNotes()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }

    suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes("%$query%")
    }
}