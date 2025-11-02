// Asked gemini to comment all the important modules for future debugging
package rick.loves.lololol.Data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey

private val baseNoteColors = listOf(
    Color(0xFF3E4E50), // Dark Slate
    Color(0xFF5A5A5A), // Medium Gray
    Color(0xFF3A506B), // Steel Blue
    Color(0xFF5E3C58), // Deep Purple
    Color(0xFF2E4057), // Midnight Blue
    Color(0xFF6B4226), // Dark Umber
    Color(0xFF336B6B)  // Dark Teal
)

fun getGradientForId(id: Int): Pair<Int, Int> {
    val baseColor = baseNoteColors[id % baseNoteColors.size]
    val endColor = baseColor.copy(
        red = baseColor.red * 0.8f,
        green = baseColor.green * 0.8f,
        blue = baseColor.blue * 0.8f
    )
    return Pair(baseColor.toArgb(), endColor.toArgb())
}

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isLocked: Boolean = false
)
