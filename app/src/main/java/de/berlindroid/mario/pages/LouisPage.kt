package de.berlindroid.mario.pages

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

@ContributesIntoSet(AppScope::class)
object LouisPage : Page {

    override val title: String = "Louis' Soundboard"
    override val author: String = "Louis"

    private val leftNames = listOf(
        "actually", "again", "ah", "and",
        "android", "apps", "can", "cats",
        "cketti", "fasciam", "german", "great",
        "happy", "i", "if", "is"
    )

    private val rightNames = listOf(
        "just", "kotlin", "like", "love",
        "make", "ok", "question", "right",
        "run", "so", "termux", "thank_you",
        "this", "to", "um", "xr"
    )

    @Composable
    override fun LeftContent() {
        Soundboard(
            title = "Mario Replicator",
            buttonNames = leftNames
        )
    }

    @Composable
    override fun RightContent() {
        Soundboard(
            title = "- Louis",
            buttonNames = rightNames
        )
    }
}

@Composable
fun Soundboard(
    title: String,
    buttonNames: List<String>
) {
    val context = LocalContext.current
    
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(16) // Allow up to 16 sounds to overlap simultaneously
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }

    val soundIds = remember { mutableStateMapOf<String, Int>() }

    LaunchedEffect(buttonNames) {
        buttonNames.forEach { name ->
            val cleanName = "sb_" + name.lowercase().trim()
            val resId = context.resources.getIdentifier(cleanName, "raw", context.packageName)
            if (resId != 0) {
                soundIds[name] = soundPool.load(context, resId, 1)
            } else {
                soundIds[name] = 0
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(buttonNames.size) { index ->
                val name = buttonNames[index]
                SoundButton(
                    label = name,
                    onClick = {
                        val soundId = soundIds[name] ?: 0
                        if (soundId != 0) {
                            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SoundButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale = if (isPressed) 0.95f else 1.0f
    val containerColor = if (isPressed) Color(0xFF383838) else Color(0xFF1E1E1E)
    val borderColor = if (isPressed) Color(0xFFCCCCCC) else Color(0xFF555555)
    val ledColor = if (isPressed) Color(0xFFFF3333) else Color(0xFF4A1212)

    val displayName = label.replace("_", " ").uppercase()

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(containerColor, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.5.dp, borderColor), RoundedCornerShape(4.dp))
            .pointerInput(onClick) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onClick()
                        try {
                            awaitRelease()
                        } finally {
                            isPressed = false
                        }
                    }
                )
            }
            .padding(8.dp)
    ) {
        // Red LED status light at top-right
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(ledColor, CircleShape)
                .align(Alignment.TopEnd)
        )

        // Button label
        Text(
            text = displayName,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
