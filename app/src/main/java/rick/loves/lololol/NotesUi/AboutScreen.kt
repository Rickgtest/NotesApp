// Asked gemini to comment all the important modules for future debugging
package rick.loves.lololol.NotesUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Align content to the top
        ) {
            // First Surface for the main text
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = "This is the first proper application I developed , be gentle. " +
                            "Used gemini a lot to blast past the errors",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp) // Padding inside the surface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val GradColors = listOf(
                Color.Green,
                Color.Yellow
            )
            val brush = Brush.linearGradient(colors = GradColors)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
            ) {
                // Layer 1: A blurred gradient background.
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(brush)
                        .blur(10.dp)
                )

                // Layer 2: A darker tint for text readability.
                Box(modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.7f)))

                // Layer 3: The text itself.
                Text(
                    text = "Version 0.1_alpha",
                    textAlign = TextAlign.Center,
                    color = Color.White, // Set text color to white for better visibility
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}