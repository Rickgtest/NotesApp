// Asked gemini to comment all the important modules for future debugging
package rick.loves.lololol.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).build()
                // For a clean install, we don't need migrations.
                // This will destroy and recreate the database on schema changes.
                //.fallbackToDestructiveMigration()
                INSTANCE = instance
                instance
            }
        }
    }
}