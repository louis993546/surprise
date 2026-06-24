package de.berlindroid.mario.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import de.berlindroid.mario.model.PageCategory
import dev.zacsweers.metro.ContributesIntoSet

@ContributesIntoSet(AppScope::class)
object IntroPage : Page {
    override val title: String = "Welcome"
    override val author: String = ""
    override val category: PageCategory = PageCategory.Cover

    private val fontFamily = FontFamily(
        Font(R.font.courier_prime)
    )

    @Composable
    override fun LeftContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.intro_thank_you_title),
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
            )
        }
    }

    @Composable
    override fun RightContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.intro_thank_you_body),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
            )
        }
    }
}
