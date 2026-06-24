package de.berlindroid.mario.pages

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private val PHOTOS = listOf(
    R.drawable.mario_pirate,
    R.drawable.mario_eating,
    R.drawable.mario_kotlinconf2025,
    R.drawable.mario_cake,
)

@ContributesIntoSet(AppScope::class)
class CkettiPage : Page {
    override val title: String = "TODO: insert title"

    override val author: String = "cketti"

    @Composable
    override fun LeftContent() {
        var currentPhoto by remember { mutableIntStateOf(PHOTOS.first()) }

        LaunchedEffect(key1 = Unit) {
            while (true) {
                for (photo in PHOTOS) {
                    currentPhoto = photo
                    delay(5.seconds)
                }
            }
        }

        Crossfade(
            targetState = currentPhoto,
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(16.dp)
                .border(3.dp, Color.Black, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
        ) { photo ->
            Image(
                painter = painterResource(photo),
                contentDescription = "Photos of Mario being Mario",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }

    @Composable
    override fun RightContent() {
        val isDeveloperState = remember { mutableStateOf(isDeveloper) }
        var emojiClickCounter by remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .safeContentPadding()
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "Thank you for the many years of fun!",
                    textAlign = TextAlign.Center,
                    fontSize = 64.sp,
                    lineHeight = 72.sp,
                )

                Spacer(modifier = Modifier.height(48.dp))

                val emoji = if (isDeveloperState.value) {
                    "\uD83D\uDC68\u200D\uD83D\uDCBB"
                } else {
                    "\uD83C\uDF89"
                }
                Text(
                    text = emoji,
                    textAlign = TextAlign.Center,
                    fontSize = 200.sp,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        emojiClickCounter++
                        if (emojiClickCounter % 3 == 0) {
                            isDeveloperState.value = false
                            isDeveloper = false

                        }
                    }
                )
            }

            BuildVersion(isDeveloperState, emojiClickCounter / 3)

            Text(
                text = "by cketti",
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    @Composable
    private fun BuildVersion(isDeveloperState: MutableState<Boolean>, resetCount: Int) {
        var clickCounter by remember(resetCount) { mutableIntStateOf(if (isDeveloper) 99 else 0) }

        MessageContainer(clickCounter)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .clip(RoundedCornerShape(16.dp))
                .clickable(enabled = clickCounter < MESSAGES.size) {
                    clickCounter++
                    if (clickCounter == DEVELOPER_CLICK_THRESHOLD) {
                        isDeveloperState.value = true
                        isDeveloper = true
                    }
                }
                .padding(16.dp)
        ) {
            Text(
                text = "Build number",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "42",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(Modifier.height(16.dp))
    }

    @Composable
    private fun MessageContainer(clickCounter: Int) {
        Box(
            contentAlignment = Alignment.BottomStart,
        ) {
            var visibleMessage by remember { mutableIntStateOf(0) }

            LaunchedEffect(key1 = clickCounter) {
                visibleMessage = clickCounter
                delay(5.seconds)
                visibleMessage = 0
            }

            MESSAGES.forEachIndexed { index, message ->
                Message(
                    text = message,
                    visible = visibleMessage == index + 1,
                )
            }
        }
    }

    @Composable
    private fun Message(
        text: String,
        visible: Boolean,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .visible(visible)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    companion object {
        private const val DEVELOPER_CLICK_THRESHOLD = 9
        private val MESSAGES = listOf(
            "You see a build number and your first instinct is to tap it? What's wrong with you?",
            "You're not stopping. I guess you've been an Android developer at some point in your life.",
            "You are now 4 steps away from being a developer.",
            "You are now 3 steps away from being a developer.",
            "You are now 3 steps away from being a developer.",
            "You are now 3 steps away from being a developer.",
            "You are now 3 steps away from being a developer.",
            "Wow, you're not giving up, are you?",
            "Okay, okay, fine. You are now a developer!",
            "You are still a developer. You can stop now.",
            "Seriously. There's nothing else this will do.",
            "That's it! Stop tapping already.",
            "What are you doing?",
            "STOP!",
            "Aren't you getting tired of this?",
            "Okay, that's it. Enough!",
        )

        private var isDeveloper = false
    }
}
