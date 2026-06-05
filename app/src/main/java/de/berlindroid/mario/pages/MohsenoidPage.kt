package de.berlindroid.mario.pages

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.mario.LocalPageIndex
import de.berlindroid.mario.LocalPagerState
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet
import kotlinx.coroutines.delay

@ContributesIntoSet(AppScope::class)
object MohsenoidPage : Page {
    override val title: String = "The Terminal Chronicles"
    override val author: String = "@mohsenoid"

    // --- Styling Constants ---
    private val TerminalGreen = Color(0xFF4AF626)
    private val TerminalBlue = Color(0xFF81D4FA)
    private val TerminalBg = Color(0xFF0C0C0C)
    private val TerminalYellow = Color(0xFFFFD54F)
    private val TerminalCyan = Color(0xFF4DD0E1)
    private val TerminalMagenta = Color(0xFFF06292)

    private val TerminalFontSizeCommand = 18.sp
    private val TerminalFontSizeOutput = 16.sp
    private val TerminalFontSizeMessage = 20.sp

    // --- Data Structures ---
    private sealed class TerminalLine {
        data class Command(val text: String) : TerminalLine()
        data class Output(val text: String) : TerminalLine()
        data object Spacer : TerminalLine()
    }

    private val leftLines = listOf(
        TerminalLine.Command("whoami"),
        TerminalLine.Output("Mario - The Berlindroid Legend"),
        TerminalLine.Spacer,
        TerminalLine.Command("uptime --community"),
        TerminalLine.Output("11 years, 0 months, 0 days"),
        TerminalLine.Output("Status: GOAT (Greatest Of All Time)"),
        TerminalLine.Spacer,
        TerminalLine.Command("ls /achievements"),
        TerminalLine.Output("zebadge_hardware_hack.sh"),
        TerminalLine.Output("zepatch_embroidery.py"),
        TerminalLine.Output("daydream_ar_workshop.so"),
        TerminalLine.Output("droidcon_booth_manager.bin"),
        TerminalLine.Output("community_heart_and_soul.dmg"),
        TerminalLine.Spacer,
        TerminalLine.Command("grep -r \"Passion\" ./berlindroid"),
        TerminalLine.Output("./berlindroid/mario.kt: val passion = Int.MAX_VALUE"),
        TerminalLine.Output("./berlindroid/mario.kt: val contribution = \"11+ Years\""),
        TerminalLine.Output("./berlindroid/mario.kt: val status = \"Legendary Organizer\""),
        TerminalLine.Spacer,
        TerminalLine.Command("history | tail -n 5"),
        TerminalLine.Output("2015: First meetup in the corner"),
        TerminalLine.Output("2018: Throwing birds as a pirate"),
        TerminalLine.Output("2021: Keeping us connected online"),
        TerminalLine.Output("2024: Teaching Software Engineering @ SRH"),
        TerminalLine.Output("2026: Handing back the GDG gun, belt, and hat"),
        TerminalLine.Spacer,
        TerminalLine.Command("mv /home/mario/berlindroid /etc/init.d/legacy"),
        TerminalLine.Output("Moving: BerlinDroid Organizer -> Freelance Lecturer & Yubico Expert"),
        TerminalLine.Output("Success: Legacy immutable. New adventure starting..."),
        TerminalLine.Spacer,
    )

    private val LeftAnimationFinished = mutableStateOf(false)

    // --- Content Composables ---

    @Composable
    override fun LeftContent() {
        val pagerState = LocalPagerState.current
        val pageIndex = LocalPageIndex.current
        val isVisible = pagerState.currentPage == pageIndex

        var visibleLines by remember { mutableStateOf<List<Pair<TerminalLine, String>>>(emptyList()) }
        var isLeftFinished by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        LaunchedEffect(isVisible) {
            if (isVisible) {
                LeftAnimationFinished.value = false
                visibleLines = emptyList()
                isLeftFinished = false

                leftLines.forEach { line ->
                    when (line) {
                        is TerminalLine.Command -> {
                            line.text.indices.forEach { i ->
                                val typed = line.text.substring(0, i + 1)
                                visibleLines = visibleLines.updateLastOrAdd(line, typed)
                                delay(80)
                            }
                            delay(500)
                        }

                        is TerminalLine.Output -> {
                            visibleLines = visibleLines + (line to line.text)
                            delay(200)
                        }

                        is TerminalLine.Spacer -> {
                            visibleLines = visibleLines + (line to "")
                        }
                    }
                }
                isLeftFinished = true
                delay(500)
                LeftAnimationFinished.value = true
            } else {
                LeftAnimationFinished.value = false
                visibleLines = emptyList()
                isLeftFinished = false
            }
        }

        LaunchedEffect(visibleLines) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TerminalBg)
                .padding(32.dp)
                .verticalScroll(scrollState)
        ) {
            visibleLines.forEachIndexed { index, (line, text) ->
                val isAnimating = index == visibleLines.size - 1 && !isLeftFinished
                when (line) {
                    is TerminalLine.Command -> Row {
                        TerminalPrompt(text)
                        if (isAnimating) TerminalCursor()
                    }

                    is TerminalLine.Output -> TerminalOutput(text)
                    is TerminalLine.Spacer -> Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (isLeftFinished) {
                BlinkingCursorPrompt()
            }
        }
    }

    @Composable
    override fun RightContent() {
        val fullMessage = """
            Executing gratitude_script.py...
            
            > Initializing farewell sequence...
            > Analyzing 11 years of impact...
            
            Dear Mario,
            
            Since I moved into Berlin I joined the community you brought together and enjoyed since then. 
            
            Thanks for all the great work all these years. We will miss your creative ideas at DroidCon Berlins.
            
            Wishing you a 'successful build' in your next adventure at SRH University and Yubico!
            
            — @mohsenoid
            
            -- End of Transmission --
        """.trimIndent()

        var visibleText by remember { mutableStateOf("") }
        val scrollState = rememberScrollState()
        val isStarted by LeftAnimationFinished

        LaunchedEffect(isStarted) {
            if (isStarted) {
                fullMessage.indices.forEach { i ->
                    visibleText = fullMessage.substring(0, i + 1)
                    delay(if (fullMessage[i] == '\n') 150 else 40)
                }
            } else {
                visibleText = ""
            }
        }

        LaunchedEffect(visibleText) {
            scrollState.scrollTo(scrollState.maxValue)
        }

        val styledMessage = remember(visibleText) {
            buildAnnotatedString {
                val lines = visibleText.split("\n")
                lines.forEachIndexed { index, line ->
                    val isHeader = index < 5
                    val isFooter = line.startsWith("--") || line.contains("Transmission")

                    if (isHeader || isFooter) {
                        append(highlightTerminalText(line))
                    } else {
                        withStyle(style = SpanStyle(color = TerminalBlue)) {
                            append(line)
                        }
                    }
                    if (index < lines.size - 1) append("\n")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TerminalBg)
                .padding(48.dp)
                .verticalScroll(scrollState)
        ) {
            if (isStarted) {
                Text(
                    text = styledMessage,
                    fontSize = TerminalFontSizeMessage,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 30.sp
                )

                if (visibleText.length == fullMessage.length) {
                    Box(modifier = Modifier.align(Alignment.End)) {
                        Text(
                            text = "SUCCESS",
                            color = TerminalGreen,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    // --- Helper Composables ---

    @Composable
    private fun TerminalPrompt(command: String) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White)) { append("mario@berlindroid") }
                withStyle(style = SpanStyle(color = TerminalBlue)) { append(":") }
                withStyle(style = SpanStyle(color = TerminalYellow)) { append("~") }
                withStyle(style = SpanStyle(color = Color.White)) { append("$ ") }
                withStyle(style = SpanStyle(color = TerminalGreen)) { append(command) }
            },
            fontFamily = FontFamily.Monospace,
            fontSize = TerminalFontSizeCommand
        )
    }

    @Composable
    private fun TerminalOutput(text: String) {
        Text(
            text = highlightTerminalText(text),
            color = Color.LightGray,
            fontFamily = FontFamily.Monospace,
            fontSize = TerminalFontSizeOutput,
            modifier = Modifier.padding(start = 16.dp)
        )
    }

    @Composable
    private fun RowScope.TerminalCursor() {
        Box(
            modifier = Modifier
                .size(width = 10.dp, height = 20.dp)
                .background(Color.White)
                .padding(start = 2.dp)
                .align(Alignment.CenterVertically)
        )
    }

    @Composable
    private fun BlinkingCursorPrompt() {
        val infiniteTransition = rememberInfiniteTransition(label = "cursor")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )

        Row {
            TerminalPrompt("")
            Box(
                modifier = Modifier
                    .size(width = 10.dp, height = 20.dp)
                    .background(Color.White.copy(alpha = alpha))
                    .align(Alignment.CenterVertically)
            )
        }
    }

    // --- Logic Helpers ---

    private fun highlightTerminalText(text: String): androidx.compose.ui.text.AnnotatedString {
        return buildAnnotatedString {
            append(text)

            // Patterns to highlight
            val highlights = listOf(
                Regex("\\.[a-zA-Z0-9]+") to TerminalCyan, // Extensions
                Regex("(/|\\./)[\\w/\\.-]+") to TerminalBlue, // Paths
                Regex("\\b(Success|SUCCESS|GOAT|Legendary Organizer|Legend|Moving)\\b") to TerminalGreen, // Status
                Regex("\\b(20\\d{2})\\b") to TerminalYellow, // Years
                Regex("val|Int\\.MAX_VALUE") to TerminalMagenta // Keywords
            )

            highlights.forEach { (regex, color) ->
                regex.findAll(text).forEach { match ->
                    addStyle(
                        SpanStyle(
                            color = color,
                            fontWeight = if (color == TerminalGreen) FontWeight.Bold else FontWeight.Normal
                        ),
                        match.range.first,
                        match.range.last + 1
                    )
                }
            }
        }
    }

    private fun List<Pair<TerminalLine, String>>.updateLastOrAdd(
        line: TerminalLine,
        text: String
    ): List<Pair<TerminalLine, String>> {
        return if (this.lastOrNull()?.first == line) {
            this.dropLast(1) + (line to text)
        } else {
            this + (line to text)
        }
    }
}
