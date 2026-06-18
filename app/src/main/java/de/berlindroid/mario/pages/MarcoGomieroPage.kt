package de.berlindroid.mario.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import de.berlindroid.mario.LocalPageIndex
import de.berlindroid.mario.LocalPagerState
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet
import kotlinx.coroutines.delay

private val ZeColor = Color(0xFFE52521)

@ContributesIntoSet(AppScope::class)
object MarcoGomieroPage : Page {

    override val title: String = "Marco Gomiero"
    override val author: String = "Marco Gomiero"

    private const val LEFT_MESSAGE =
        "Thank you Mario for all the amazingness you brought to the Android community."

    private const val RIGHT_MESSAGE =
        "The way we met fits your awesomeness: in a plane to droidcon Romania sitting close without knowing! ✈️"

    private const val RIGHT_CLOSING = "We will miss you!"

    private const val SIGNATURE = "- Marco Gomiero"

    @Composable
    override fun LeftContent() {
        ZePrefixedMessage(
            message = LEFT_MESSAGE,
            modifier = Modifier.fillMaxSize(),
        )
    }

    @Composable
    override fun RightContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = RIGHT_MESSAGE,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = RIGHT_CLOSING,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = SIGNATURE,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun ZePrefixedMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    val words = remember(message) { message.split(" ") }
    var zeWordCount by remember { mutableIntStateOf(0) }

    val pagerState = LocalPagerState.current
    val pageIndex = LocalPageIndex.current
    val isVisible = pagerState.currentPage == pageIndex

    LaunchedEffect(isVisible) {
        if (isVisible) {
            zeWordCount = 0
            delay(3_000)
            words.indices.forEach { index ->
                delay(120)
                zeWordCount = index + 1
            }
        } else {
            zeWordCount = 0
        }
    }

    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = buildAnnotatedString {
                words.forEachIndexed { index, word ->
                    if (index > 0) append(" ")
                    if (index < zeWordCount) {
                        withStyle(
                            SpanStyle(
                                color = ZeColor,
                                fontWeight = FontWeight.ExtraBold,
                            ),
                        ) {
                            append("Ze")
                        }
                    }
                    append(word)
                }
            },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
    }
}
