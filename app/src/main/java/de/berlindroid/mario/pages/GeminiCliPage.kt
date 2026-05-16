package de.berlindroid.mario.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

@ContributesIntoSet(AppScope::class)
object GeminiCliPage : Page {
    override val title: String = "From the CLI"
    override val author: String = "Gemini CLI"

    @Composable
    override fun LeftContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A237E)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "> Hello Mario,",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }

    @Composable
    override fun RightContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D47A1)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Wishing you all the best!",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 32.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "System.out.println(\"Luck!\");",
                    color = Color(0xFF81D4FA),
                    fontSize = 24.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
