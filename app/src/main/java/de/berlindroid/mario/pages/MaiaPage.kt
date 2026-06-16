package de.berlindroid.mario.pages

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

data class Bubble(
    val id: Int,
    val xFraction: Float,
    val yFraction: Float,
    val radiusDp: Float,
    val color: Color,
    val isPopped: Boolean = false
)

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

    private fun generateRandomBubbles(): List<Bubble> {
        val colors = listOf(
            Color(0xBB00E5FF), // Vibrant Cyan
            Color(0xBBD500F9), // Vibrant Purple
            Color(0xBBFF4081), // Vibrant Pink
            Color(0xBBFFAB00), // Vibrant Orange
            Color(0xBB00E676), // Vibrant Green
            Color(0xBBFFEA00), // Vibrant Yellow
            Color(0xBB2979FF), // Vibrant Blue
            Color(0xBBFF3D00)  // Vibrant Red/Orange
        )
        return List(120) { id ->
            Bubble(
                id = id,
                xFraction = kotlin.random.Random.nextFloat(),
                yFraction = 0.05f + kotlin.random.Random.nextFloat() * 0.8f,
                radiusDp = 25f + kotlin.random.Random.nextFloat() * 55f, // Sizes 25dp to 80dp
                color = colors.random()
            )
        }
    }

    @Composable
    override fun LeftContent() {
        var bubbles by remember { mutableStateOf(generateRandomBubbles()) }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            val width = maxWidth
            val height = maxHeight

            // 1. Secret message underneath the bubbles
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
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

            // 2. Interactive Bubbles layer
            bubbles.forEach { bubble ->
                val scale by animateFloatAsState(
                    targetValue = if (bubble.isPopped) 0f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "bubble_scale"
                )

                if (scale > 0.01f) {
                    val x = width * bubble.xFraction
                    val y = height * bubble.yFraction
                    val size = (bubble.radiusDp * 2 * scale).dp

                    Box(
                        modifier = Modifier
                            .offset(
                                x = x - (bubble.radiusDp * scale).dp,
                                y = y - (bubble.radiusDp * scale).dp
                            )
                            .size(size)
                            .clip(CircleShape)
                            .background(bubble.color)
                            .clickable(enabled = !bubble.isPopped) {
                                bubbles = bubbles.map {
                                    if (it.id == bubble.id) it.copy(isPopped = true) else it
                                }
                            }
                    )
                }
            }

            // 3. Control Buttons at the bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        bubbles = bubbles.map { it.copy(isPopped = true) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Pop All",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        bubbles = generateRandomBubbles()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
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
