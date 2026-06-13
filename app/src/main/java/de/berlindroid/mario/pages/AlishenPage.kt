package de.berlindroid.mario.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

@ContributesIntoSet(AppScope::class)
object AlishenPage : Page {

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

    override val title = "Alishen Page"
    override val author = "Alishen"

    @Composable
    override fun LeftContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Thanks for everything, Mario,",
                fontFamily = fontFamily,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "I joined Berlindroid in 2019 —\nyou had already been at it for years.",
                fontFamily = fontFamily,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "Dein Einsatz ist unbezahlbar.\nDanke für alles, Mario.",
                fontFamily = fontFamily,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    override fun RightContent() {
        AsyncImage(
            model = R.drawable.alishen_droidcon_2025,
            contentDescription = "ZePatch booth at droidcon Berlin 2025",
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .clip(RoundedCornerShape(32.dp)),
            contentScale = ContentScale.Crop
        )
    }
}