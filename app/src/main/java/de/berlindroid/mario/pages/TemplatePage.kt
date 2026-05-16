package de.berlindroid.mario.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

/**
 * Template for a new page in the farewell book.
 *
 * To add your own page:
 * 1. Copy this file and rename it (e.g., YourNamePage.kt).
 * 2. Change the object name.
 * 3. Add @ContributesIntoSet(AppScope::class) to the object.
 * 4. Implement your own Content composable.
 */
@ContributesIntoSet(AppScope::class)
object TemplatePage : Page {

    override val title: String = "Template Page"
    override val author: String = "Your Name"

    @Composable
    override fun LeftContent() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Left Message",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }

    @Composable
    override fun RightContent() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Right Message",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
