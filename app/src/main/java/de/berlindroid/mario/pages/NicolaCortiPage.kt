package de.berlindroid.mario.pages

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import de.berlindroid.mario.model.PageCategory
import dev.zacsweers.metro.ContributesIntoSet
import kotlin.math.cos
import kotlin.math.sin

private val TermGreen = Color(0xFF3DDC84)
private val TermDark = Color(0xFF0D1117)
private val TermDarker = Color(0xFF060A0F)
private val TermGreenDim = Color(0xFF1A6B3C)
private val TermGray = Color(0xFF8B949E)
private val TermBlue = Color(0xFF58A6FF)
private val TermYellow = Color(0xFFE3B341)
private val TermWhite = Color(0xFFE6EDF3)
private val TermSurface = Color(0xFF161B22)

private val TerminalScript = listOf(
    "$ ./thank_you.sh --recipient mario",
    "",
    "> Resolving dependencies...",
    "> Loading years of community work... [OK]",
    "> Counting meetups organized......... [∞]",
    "> Counting developers inspired........ [∞]",
    "> Measuring community impact.......... [∞]",
    "",
    "> Running checks:",
    ">   inspiring talks.............. PASS ✓",
    ">   welcoming newcomers.......... PASS ✓",
    ">   building the community........ PASS ✓",
    ">   being irreplaceable........... PASS ✓",
    "",
    "> BUILD SUCCESSFUL",
    "> STATUS: LEGENDARY",
    "",
    "// The Android world is a better place",
    "// because of you. Thank you, Mario.",
    "//",
    "//   — Nicola Corti",
)

@ContributesIntoSet(AppScope::class)
object NicolaCortiPage : Page {
    override val title: String = "Terminal Farewell"
    override val author: String = "Nicola Corti"
    override val category: PageCategory = PageCategory.Community

    @Composable
    override fun LeftContent() {
        TerminalPage()
    }

    @Composable
    override fun RightContent() {
        TributePage()
    }
}

@Composable
private fun TerminalPage() {
    val transition = rememberInfiniteTransition(label = "nicola_left")

    val typingProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 14000
                0f at 0
                1f at 9000
                1f at 14000
            }
        ),
        label = "typing"
    )
    val cursor by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(540, easing = LinearEasing), RepeatMode.Reverse),
        label = "cursor"
    )
    val scanLine by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)),
        label = "scan"
    )

    val allText = TerminalScript.joinToString("\n")
    val visibleLength = (allText.length * typingProgress).toInt().coerceIn(0, allText.length)
    val visibleText = allText.take(visibleLength)
    val cursorChar = if (cursor > 0.5f) "█" else " "
    val scrollState = rememberScrollState()
    LaunchedEffect(visibleLength) { scrollState.animateScrollTo(scrollState.maxValue) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(TermGreenDim.copy(alpha = 0.18f), TermDarker),
                    center = Offset(400f, 300f),
                    radius = 1600f
                )
            )
    ) {
        val termFontSize = (maxWidth.value / 32f).coerceIn(10f, 19f).sp
        val termLineHeight = (termFontSize.value * 1.55f).sp
        val chromeFontSize = (maxWidth.value / 60f).coerceIn(8f, 15f).sp
        val hPad = (maxWidth.value * 0.06f).coerceIn(16f, 52f).dp
        val vPad = (maxHeight.value * 0.05f).coerceIn(12f, 44f).dp
        val innerPad = (maxWidth.value * 0.04f).coerceIn(12f, 36f).dp
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Subtle CRT scanlines
            var y = 0f
            while (y < size.height) {
                drawRect(
                    color = Color.Black.copy(alpha = 0.07f),
                    topLeft = Offset(0f, y),
                    size = Size(size.width, 1.5f)
                )
                y += 6f
            }
            // Moving glow line
            val glowY = size.height * scanLine
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, TermGreen.copy(alpha = 0.05f), Color.Transparent),
                    startY = glowY - 80f,
                    endY = glowY + 80f
                ),
                topLeft = Offset(0f, glowY - 80f),
                size = Size(size.width, 160f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = hPad, vertical = vPad)
        ) {
            // Window chrome
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TrafficDot(Color(0xFFFF5F57))
                    TrafficDot(Color(0xFFFFBD2E))
                    TrafficDot(Color(0xFF28CA41))
                }
                Text(
                    text = "bash — 80×48",
                    color = TermGray,
                    fontFamily = FontFamily.Monospace,
                    fontSize = chromeFontSize
                )
            }

            Spacer(modifier = Modifier.height(vPad / 2))

            // Terminal body — scrollable
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(20.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(TermDark)
                    .border(1.dp, TermGreen.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                    .padding(innerPad)
            ) {
                Text(
                    text = visibleText + cursorChar,
                    color = TermGreen,
                    fontFamily = FontFamily.Monospace,
                    fontSize = termFontSize,
                    lineHeight = termLineHeight,
                    modifier = Modifier.verticalScroll(scrollState)
                )
            }

            Spacer(modifier = Modifier.height(vPad / 2))

            Text(
                text = "nicola@android:~$",
                color = TermGray.copy(alpha = 0.5f),
                fontFamily = FontFamily.Monospace,
                fontSize = chromeFontSize
            )
        }
    }
}

@Composable
private fun TrafficDot(color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        drawCircle(color = color, radius = size.minDimension / 2f)
        drawCircle(
            color = Color.Black.copy(alpha = 0.25f),
            radius = size.minDimension / 2f,
            style = Stroke(width = 1.5f)
        )
    }
}

@Composable
private fun TributePage() {
    val transition = rememberInfiniteTransition(label = "nicola_right")

    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulse"
    )
    val orbit by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing)),
        label = "orbit"
    )
    val blink by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Reverse),
        label = "blink"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(TermDark, Color(0xFF0A1628), TermDarker)
                )
            )
    ) {
        // Orbital rings background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width * 0.5f
            val cy = size.height * 0.36f

            repeat(5) { i ->
                drawCircle(
                    color = TermGreen.copy(alpha = 0.04f + i * 0.015f),
                    radius = size.minDimension * (0.16f + i * 0.08f),
                    center = Offset(cx, cy),
                    style = Stroke(width = 1.5f)
                )
            }

            // Orbiting dot with glow
            val angle = orbit * 2f * Math.PI.toFloat()
            val orbitR = size.minDimension * 0.30f
            val dotX = cx + cos(angle) * orbitR
            val dotY = cy + sin(angle) * orbitR * 0.45f
            drawCircle(color = TermGreen.copy(alpha = 0.18f + pulse * 0.12f), radius = 28f + pulse * 10f, center = Offset(dotX, dotY))
            drawCircle(color = TermGreen, radius = 10f, center = Offset(dotX, dotY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Android robot
            AndroidBot(
                modifier = Modifier.size(120.dp),
                glowAlpha = 0.35f + pulse * 0.25f,
                eyeAlpha = 0.7f + blink * 0.3f
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Message card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(TermSurface)
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                TermGreen.copy(alpha = 0.1f + pulse * 0.2f),
                                TermBlue.copy(alpha = 0.15f + pulse * 0.1f),
                                TermGreen.copy(alpha = 0.1f + pulse * 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 28.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "The Android world is a better place",
                        color = TermWhite,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )
                    Text(
                        text = "because of you.",
                        color = TermGreen,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Thank you, Mario.",
                        color = TermYellow,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )

                }
            }
        }

        // Always-visible attribution pinned to bottom
        Text(
            text = "by Nicola Corti · @cortinico",
            color = TermGray.copy(alpha = 0.5f),
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun CommitBadge(label: String, color: Color) {
    Text(
        text = label,
        color = color,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    )
}

@Composable
private fun AndroidBot(
    modifier: Modifier = Modifier,
    glowAlpha: Float = 0.5f,
    eyeAlpha: Float = 1f
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.045f
        val stroke = Stroke(width = strokeW, cap = StrokeCap.Round)

        // Outer glow
        drawCircle(
            color = TermGreen.copy(alpha = glowAlpha * 0.25f),
            radius = w * 0.54f,
            center = Offset(w * 0.5f, h * 0.54f)
        )

        // Antennas
        val antLX = w * 0.29f
        val antRX = w * 0.71f
        val antBaseY = h * 0.30f
        drawLine(TermGreen, Offset(antLX, antBaseY), Offset(w * 0.19f, h * 0.08f), strokeW, StrokeCap.Round)
        drawLine(TermGreen, Offset(antRX, antBaseY), Offset(w * 0.81f, h * 0.08f), strokeW, StrokeCap.Round)
        drawCircle(TermGreen, radius = strokeW * 1.1f, center = Offset(w * 0.19f, h * 0.08f))
        drawCircle(TermGreen, radius = strokeW * 1.1f, center = Offset(w * 0.81f, h * 0.08f))

        // Head
        val headL = w * 0.10f
        val headT = h * 0.28f
        val headR = w * 0.90f
        val headB = h * 0.68f
        drawRoundRect(
            color = TermGreen.copy(alpha = 0.12f),
            topLeft = Offset(headL, headT),
            size = Size(headR - headL, headB - headT),
            cornerRadius = CornerRadius(w * 0.18f)
        )
        drawRoundRect(
            color = TermGreen,
            topLeft = Offset(headL, headT),
            size = Size(headR - headL, headB - headT),
            cornerRadius = CornerRadius(w * 0.18f),
            style = stroke
        )

        // Eyes
        val eyeY = h * 0.48f
        val eyeR = strokeW * 1.3f
        drawCircle(TermGreen.copy(alpha = eyeAlpha), radius = eyeR, center = Offset(w * 0.35f, eyeY))
        drawCircle(TermGreen.copy(alpha = eyeAlpha), radius = eyeR, center = Offset(w * 0.65f, eyeY))

        // Body
        val bodyL = w * 0.15f
        val bodyT = h * 0.70f
        val bodyR = w * 0.85f
        val bodyB = h * 0.96f
        drawRoundRect(
            color = TermGreen.copy(alpha = 0.10f),
            topLeft = Offset(bodyL, bodyT),
            size = Size(bodyR - bodyL, bodyB - bodyT),
            cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
        )
        drawRoundRect(
            color = TermGreen,
            topLeft = Offset(bodyL, bodyT),
            size = Size(bodyR - bodyL, bodyB - bodyT),
            cornerRadius = CornerRadius(w * 0.06f, w * 0.06f),
            style = stroke
        )

        // Arms
        drawLine(TermGreen, Offset(bodyL, bodyT + (bodyB - bodyT) * 0.2f), Offset(headL - strokeW * 1.5f, bodyT + (bodyB - bodyT) * 0.2f), strokeW, StrokeCap.Round)
        drawLine(TermGreen, Offset(bodyR, bodyT + (bodyB - bodyT) * 0.2f), Offset(headR + strokeW * 1.5f, bodyT + (bodyB - bodyT) * 0.2f), strokeW, StrokeCap.Round)
    }
}
