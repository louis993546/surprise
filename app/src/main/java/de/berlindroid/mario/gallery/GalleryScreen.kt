package de.berlindroid.mario.gallery

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import de.berlindroid.mario.R
import de.berlindroid.mario.model.rememberSlideshowDelayState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val GALLERY_PHOTOS = listOf(
    R.drawable.gallery_1,
    R.drawable.gallery_2,
    R.drawable.gallery_3,
    R.drawable.gallery_4,
    R.drawable.gallery_5,
    R.drawable.gallery_6,
    R.drawable.gallery_7,
    R.drawable.gallery_8,
    R.drawable.gallery_9,
    R.drawable.gallery_10,
    R.drawable.gallery_11,
    R.drawable.gallery_12,
    R.drawable.gallery_13,
    R.drawable.gallery_14,
    R.drawable.gallery_15,
    R.drawable.gallery_16,
    R.drawable.gallery_18,
    R.drawable.alishen_droidcon_2025,
    R.drawable.ellie,
    R.drawable.james_funcode,
    R.drawable.timetoplay_left,
    R.drawable.lucas_zebadge,
    R.drawable.mario_cake,
    R.drawable.mario,
    R.drawable.mario_eating,
    R.drawable.mario_pirate,
    R.drawable.mario_kotlinconf2025,
)

@Composable
fun GalleryScreen(onExit: () -> Unit) {
    var currentIndex by remember { mutableIntStateOf(GALLERY_PHOTOS.indices.random()) }
    val delayMs by rememberSlideshowDelayState()

    LaunchedEffect(delayMs) {
        while (true) {
            delay(delayMs.milliseconds)
            if (GALLERY_PHOTOS.size > 1) {
                var nextIndex = currentIndex
                while (nextIndex == currentIndex) {
                    nextIndex = GALLERY_PHOTOS.indices.random()
                }
                currentIndex = nextIndex
            } else {
                currentIndex = 0
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onExit() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )

        Crossfade(
            targetState = GALLERY_PHOTOS[currentIndex],
            animationSpec = tween(1000), // 1 second transition
            modifier = Modifier.fillMaxSize()
        ) { drawableId ->
            Image(
                painter = painterResource(drawableId),
                contentDescription = "Gallery Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}
