package de.berlindroid.mario.pages

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

@ContributesIntoSet(AppScope::class)
object LucasVillaVerdePage : Page {

    override val title: String = "You're part of history"
    override val author: String = "Lucas Villa Verde"

    @Composable
    override fun LeftContent() {
        val infiniteTransition = rememberInfiniteTransition(label = "dreamy")

        // Animate an offset value between 0 and 1000f to shift the gradient
        val offset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "offset"
        )

        val dreamyGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE1BEE7),
                Color(0xFFFFCCBC),
                Color(0xFFFFF9C4),
                Color(0xFFB3E5FC)
            ),
            startY = 0f,
            endY = offset // The gradient shifts based on the animated value
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = R.drawable.lucas_zebadge,
                contentScale = ContentScale.Crop,
                contentDescription = "Lucas ZeBadge thumbs up"
            )

            Column(
                modifier = Modifier
                    .background(
                        brush = dreamyGradient, alpha = 0.7F,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text(
                    text = messageStart,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = messageBody,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    @Composable
    override fun RightContent() {
        val infiniteTransition = rememberInfiniteTransition(label = "chaos")

        // Let's make it spin with a "stutter" to look more chaotic
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation"
        )

        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "GDG Organizer Truths", // Punchier title
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .background(color = Color(0xFF4285F4), shape = CircleShape)
                            .rotate(rotation)
                            .padding(4.dp),
                        tint = Color.White,
                        imageVector = Icons.Default.Warning, // Changed to Warning for extra flavor
                        contentDescription = "Chaos icon"
                    )
                }
                Text(
                    text = "The stuff they don't put on the brochure:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))

                organizerFacts.forEach { fact ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Text(
                            text = fact,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }

    // Text
    private const val messageStart = "Dear Mario,"
    val messageBody = buildString {
        append("I can still remember how I first got to know you. I was still a little boy, didn't know anything about conferences and felt lost, hopeless and scared.")
        append(" But there was only one way I could be saved, if only I had a super hero, a master, a guide to pull me out of the darkness...")
        appendLine(" And you were there!")
        append("I suppose many people felt or still feel like I felt and you saved them, made sure they were safe and gave them purpose.")
        appendLine("Thank you for all those years of dedication, there are no words to describe how much impact you brought to peoples lives.")
        appendLine("As of today, there's no way we can all imagine a world without Mario.")
        appendLine()
        appendLine("Wishing you all the best!")
    }

    private val organizerFacts = listOf(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("The 'Rubber Duck' Therapist: ") }
            append("The organizer spends more time debugging social meltdowns than fixing Android Fragment nightmares.")
        },
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("The 'Feature Creep' Wrangler: ") }
            append("They keep the group grounded by reminding everyone that adding Jetpack Compose won't magically save a broken app.")
        },
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("The 'Chaos Experimenter': ") }
            append("The organizer’s main job is encouraging members to 'break things on purpose' just to see if their apps explode in creative ways.")
        }
    )

    // Colors
    val dreamySunsetPalette = listOf(
        Color(0xFFE1BEE7), // Soft Lavender
        Color(0xFFFFCCBC), // Pale Peach
        Color(0xFFFFF9C4), // Dreamy Cream
        Color(0xFFB3E5FC)  // Soft Sky Blue
    )
}

@Preview
@Composable
private fun LucasLeftPagePreview() {
    Box(
        modifier = Modifier
            .width(550.dp)
            .height(400.dp)
    ) {
        LucasVillaVerdePage.LeftContent()
    }
}

@Preview
@Composable
private fun LucasRightPagePreview() {
    Box(
        modifier = Modifier
            .width(550.dp)
            .height(400.dp)
    ) {
        LucasVillaVerdePage.RightContent()
    }
}
