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
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

@ContributesIntoSet(AppScope::class)
object LouisPage : Page {

    override val title: String = "Soundboard"
    override val author: String = "Louis"

    private val leftSounds = listOf(
        "actually" to R.raw.sb_actually,
        "again" to R.raw.sb_again,
        "ah" to R.raw.sb_ah,
        "and" to R.raw.sb_and,
        "android" to R.raw.sb_android,
        "apps" to R.raw.sb_apps,
        "can" to R.raw.sb_can,
        "cats" to R.raw.sb_cats,
        "cketti" to R.raw.sb_cketti,
        "fascism" to R.raw.sb_fascism,
        "german" to R.raw.sb_german,
        "great" to R.raw.sb_great,
        "happy" to R.raw.sb_happy,
        "i" to R.raw.sb_i,
        "if" to R.raw.sb_if,
        "is" to R.raw.sb_is
    )

    private val rightSounds = listOf(
        "just" to R.raw.sb_just,
        "kotlin" to R.raw.sb_kotlin,
        "like" to R.raw.sb_like,
        "love" to R.raw.sb_love,
        "make" to R.raw.sb_make,
        "ok" to R.raw.sb_ok,
        "question" to R.raw.sb_question,
        "right" to R.raw.sb_right,
        "run" to R.raw.sb_run,
        "so" to R.raw.sb_so,
        "termux" to R.raw.sb_termux,
        "thank_you" to R.raw.sb_thank_you,
        "this" to R.raw.sb_this,
        "to" to R.raw.sb_to,
        "um" to R.raw.sb_um,
        "xr" to R.raw.sb_xr
    )

    @Composable
    override fun LeftContent() {
        Soundboard(
            title = "Mario Replicator",
            sounds = leftSounds
        )
    }

    @Composable
    override fun RightContent() {
        Soundboard(
            title = "Mario Replicator",
            sounds = rightSounds,
            byline = "by Louis"
        )
    }
}

@Composable
fun Soundboard(
    title: String,
    sounds: List<Pair<String, Int>>,
    byline: String? = null
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

    LaunchedEffect(sounds) {
        sounds.forEach { (name, resId) ->
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                items(sounds.size) { index ->
                    val (name, _) = sounds[index]
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

        if (byline != null) {
            Text(
                text = byline,
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
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
