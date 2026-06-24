package de.berlindroid.mario.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

/**
 * Template for a new page in the thank you book.
 *
 * To add your own page:
 * 1. Copy this file and rename it (e.g., YourNamePage.kt).
 * 2. Change the object name.
 * 3. Add @ContributesIntoSet(AppScope::class) to the object.
 * 4. Implement your own Content composable.
 */
@ContributesIntoSet(AppScope::class)
object LouisPage : Page {

    override val title: String = "Louis"
    override val author: String = "Louis"

    @Composable
    override fun LeftContent() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        }
    }

    @Composable
    override fun RightContent() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        }
    }
}
