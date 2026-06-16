package de.berlindroid.mario.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

@ContributesIntoSet(AppScope::class)
object MaiaPage : Page {

    private val fontFamily by lazy(LazyThreadSafetyMode.NONE) {
        FontFamily(
            Font(
                googleFont = GoogleFont("Caveat"),
                fontProvider = GoogleFont.Provider(
                    providerAuthority = "com.google.android.gms.fonts",
                    providerPackage = "com.google.android.gms",
                    certificates = R.array.com_google_android_gms_fonts_certs
                ),
            )
        )
    }

    override val title: String = "Maia's Message"
    override val author: String = "Maia"

    @Composable
    override fun LeftContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Dear Mario,\n\nyou will be missed but I am sure you will have a huge impact on all your future projects.\n\nThanks for everything,\nMaia",
                    fontFamily = fontFamily,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.displayMedium.lineHeight * 1.2
                )
            }
        }
    }

    @Composable
    override fun RightContent() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Coming soon...",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
