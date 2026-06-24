package de.berlindroid.mario.pages

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import de.berlindroid.mario.model.PageCategory
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape
import dev.zacsweers.metro.ContributesIntoSet
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@ContributesIntoSet(AppScope::class)
object JamesCullimorePage : Page {
    override val title: String = "ZeBadge to Fun Code"
    override val author: String = "James Cullimore"
    override val category: PageCategory = PageCategory.Community

    @Composable
    override fun LeftContent() {
        BadgeDispatchPage()
    }

    @Composable
    override fun RightContent() {
        MagazineTributePage()
    }
}

@Composable
private fun BadgeDispatchPage() {
    val motion = rememberFarewellMotion()
    val dispatch = "MARIO STATUS UPDATE\n\nOrganizer mode: complete\nCommunity legend mode: enabled\nGDG Berlin will miss you."

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(ConsoleGreen.copy(alpha = 0.42f), ConsoleDark),
                    center = Offset(320f, 220f),
                    radius = 1300f
                )
            )
            .padding(horizontal = 64.dp, vertical = 46.dp)
    ) {
        LibraryParticleField(
            colors = listOf(Color.White.copy(alpha = 0.45f), GdgYellow, GdgGreen),
            centerX = 610.dp,
            centerY = 70.dp,
            width = 900.dp,
            height = 120.dp,
            gravityAngle = 0,
            particlesPerSecond = 26,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DROIDCON BERLIN FIELD TRANSMISSION",
                color = EInk,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(22.dp))
            BadgeScreen(
                text = dispatch,
                typingProgress = motion.typingProgress,
                cursorAlpha = motion.cursor,
                scan = motion.scan
            )
            Spacer(modifier = Modifier.height(20.dp))
            ZeBadgeShowcase(motion = motion)
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Sticker("Certified\nZeBadge Survivor", GdgYellow, Color.Black)
                Sticker("Organizer ->\nLegend", GdgGreen, Color.White)
            }
        }
    }
}

@Composable
private fun BadgeScreen(
    text: String,
    typingProgress: Float,
    cursorAlpha: Float,
    scan: Float
) {
    val typedLength = (text.length * typingProgress).toInt().coerceIn(0, text.length)
    val typedText = text.take(typedLength)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(216.dp)
            .shadow(14.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(EInk)
            .border(6.dp, Color.Black.copy(alpha = 0.78f), RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        EInkTexture(scan = scan, modifier = Modifier.fillMaxSize())
        Text(
            text = typedText + if (cursorAlpha > 0.5f) "█" else "",
            color = Color(0xFF172117),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 27.sp
        )
    }
}

@Composable
private fun ZeBadgeShowcase(
    motion: FarewellMotion,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(272.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val board = androidx.compose.ui.geometry.Rect(
                left = size.width * 0.08f,
                top = size.height * 0.16f,
                right = size.width * 0.92f,
                bottom = size.height * 0.82f
            )
            val scanX = board.left + board.width * motion.scan
            repeat(7) { index ->
                val y = board.top + board.height * (0.13f + index * 0.12f)
                drawLine(
                    color = GdgGreen.copy(alpha = 0.24f),
                    start = Offset(board.left - 42f, y),
                    end = Offset(board.right + 42f, y),
                    strokeWidth = 2f
                )
            }
            repeat(5) { index ->
                val x = board.left + board.width * (0.1f + index * 0.2f)
                val ledAlpha = if (((motion.drift * 10).toInt() + index) % 2 == 0) 0.9f else 0.28f
                drawCircle(
                    color = listOf(GdgYellow, GdgGreen, GdgBlue, GdgRed, Color.White)[index].copy(alpha = ledAlpha),
                    radius = 8f,
                    center = Offset(x, board.bottom + 34f)
                )
            }
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, ConsoleGreen.copy(alpha = 0.34f), Color.Transparent),
                    startX = scanX - 28f,
                    endX = scanX + 28f
                ),
                topLeft = Offset(scanX - 28f, board.top),
                size = Size(56f, board.height)
            )
        }

        Image(
            painter = painterResource(R.drawable.james_sticker),
            contentDescription = "James peeking from behind the ZeBadge card",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 260.dp, y = (-104).dp)
                .size(96.dp)
                .graphicsLayer {
                    alpha = motion.peekAlpha
                    translationX = 86f - motion.peekTravel * 112f
                    rotationZ = -2f - motion.peekTravel * 12f
                    scaleX = 0.9f + motion.peekAlpha * 0.12f
                    scaleY = 0.9f + motion.peekAlpha * 0.12f
                }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(172.dp)
                .graphicsLayer {
                    rotationZ = -0.9f + motion.floaty * 1.4f
                    translationY = motion.floaty * 5f
                    scaleX = 0.99f + motion.pulse * 0.018f
                    scaleY = 0.99f + motion.pulse * 0.018f
                }
                .shadow(16.dp, RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFEDEFE8))
                .border(4.dp, Color.Black.copy(alpha = 0.68f), RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.james_zebadge),
                contentDescription = "ZeBadge conference badge",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                val shineX = size.width * (-0.25f + motion.scan * 1.5f)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.26f), Color.Transparent)
                    ),
                    topLeft = Offset(shineX, 0f),
                    size = Size(size.width * 0.18f, size.height)
                )
            }
        }

        Text(
            text = "ZeBadge boot sequence",
            color = EInk,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.54f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun MagazineTributePage() {
    val motion = rememberFarewellMotion()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8EFC3))
    ) {
        MagazineBands(progress = motion.drift, modifier = Modifier.fillMaxSize())
        LibraryParticleField(
            colors = listOf(GdgBlue, GdgRed, GdgYellow, GdgGreen, Color.White),
            centerX = 560.dp,
            centerY = 84.dp,
            width = 1080.dp,
            height = 120.dp,
            gravityAngle = 0,
            particlesPerSecond = 40,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 58.dp, top = 42.dp, end = 58.dp, bottom = 42.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "fun code",
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "farewell edition",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MagazineCard(motion = motion, modifier = Modifier.weight(0.9f))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    VoltronCard(motion = motion)
                    Sticker("Talks\nenjoyed", GdgBlue, Color.White)
                    Sticker("Meetups\nsadly missed", GdgRed, Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(10.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .border(2.dp, Color.Black.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 30.dp, vertical = 24.dp)
            ) {
                Text(
                    text = FarewellMessage,
                    color = Color(0xFF1B1B1B),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
private fun MagazineCard(
    motion: FarewellMotion,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(300.dp)
            .graphicsLayer {
                rotationZ = -3.2f + motion.floaty * 1.6f
                translationY = motion.floaty * 7f
            }
            .shadow(14.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(R.drawable.james_funcode),
            contentDescription = "fun code magazine",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val shineX = size.width * (-0.4f + motion.scan * 1.8f)
            val shine = Path().apply {
                moveTo(shineX, 0f)
                lineTo(shineX + size.width * 0.24f, 0f)
                lineTo(shineX + size.width * 0.05f, size.height)
                lineTo(shineX - size.width * 0.18f, size.height)
                close()
            }
            drawPath(
                path = shine,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.34f), Color.Transparent)
                )
            )
        }
    }
}

@Composable
private fun VoltronCard(
    motion: FarewellMotion,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.86f)
            .height(178.dp)
            .graphicsLayer {
                rotationZ = 1.6f - motion.floaty * 1.2f
                translationY = -motion.floaty * 5f
            }
            .shadow(10.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.88f))
            .border(2.dp, Color.Black.copy(alpha = 0.14f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.james_voltron),
            contentDescription = "GDG Berlin Android Voltron",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val y = size.height * (0.12f + motion.scan * 0.76f)
            drawLine(
                color = GdgBlue.copy(alpha = 0.38f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 5f
            )
        }
    }
}

@Composable
private fun Sticker(
    text: String,
    background: Color,
    foreground: Color
) {
    Text(
        text = text,
        color = foreground,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Black,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .shadow(6.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .border(2.dp, Color.Black.copy(alpha = 0.18f), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Composable
private fun EInkTexture(
    scan: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val rowHeight = 8f
        var y = 0f
        while (y < size.height) {
            drawRect(
                color = Color.Black.copy(alpha = if (((y / rowHeight).toInt() % 2) == 0) 0.06f else 0.02f),
                topLeft = Offset(0f, y),
                size = Size(size.width, 2f)
            )
            y += rowHeight
        }

        val scanY = size.height * scan
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.42f), Color.Transparent),
                startY = scanY - 34f,
                endY = scanY + 34f
            ),
            topLeft = Offset(0f, scanY - 34f),
            size = Size(size.width, 68f)
        )

        repeat(120) { index ->
            val x = ((index * 47) % 997) / 997f * size.width
            val dotY = ((index * 83) % 811) / 811f * size.height
            val alpha = 0.04f + ((index % 5) * 0.012f)
            drawCircle(Color.Black.copy(alpha = alpha), radius = 1.1f, center = Offset(x, dotY))
        }
    }
}

@Composable
private fun LibraryParticleField(
    colors: List<Color>,
    centerX: Dp,
    centerY: Dp,
    width: Dp,
    height: Dp,
    gravityAngle: Int,
    particlesPerSecond: Int,
    modifier: Modifier = Modifier
) {
    CanvasParticleEmitter(
        modifier = modifier,
        config = CanvasEmitterConfig(
            particlePerSecond = particlesPerSecond,
            emitterCenter = DpOffset(centerX, centerY),
            startRegionShape = CanvasEmitterConfig.Shape.RECT,
            startRegionSize = DpSize(width, height),
            particleShapes = listOf(ParticleShape.Circle),
            lifespanRange = 1800..3600,
            fadeOutTime = 700..1400,
            scaleTime = 500..1100,
            colors = colors,
            particleSizes = listOf(
                DpSize(3.dp, 3.dp),
                DpSize(5.dp, 5.dp),
                DpSize(8.dp, 8.dp)
            ),
            spread = -120..120,
            blendMode = BlendMode.Screen,
            initialForce = 28..96,
            gravityStrength = 36f,
            gravityAngle = gravityAngle
        )
    )
}

@Composable
private fun MagazineBands(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFE91E63), Color(0xFF7C4DFF), Color(0xFFFFF176)),
            )
        )

        val topBand = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 0.19f)
            lineTo(0f, size.height * 0.32f)
            close()
        }
        drawPath(topBand, Color.Black.copy(alpha = 0.24f))

        val diagonal = Path().apply {
            moveTo(size.width * 0.02f, size.height * 0.66f)
            lineTo(size.width, size.height * 0.45f)
            lineTo(size.width, size.height * 0.56f)
            lineTo(0f, size.height * 0.78f)
            close()
        }
        drawPath(diagonal, Color.White.copy(alpha = 0.45f))

        val orbitCenter = Offset(size.width * 0.82f, size.height * 0.26f)
        repeat(5) { index ->
            val radius = size.minDimension * (0.11f + index * 0.034f)
            drawCircle(
                color = listOf(GdgGreen, GdgYellow, GdgRed, GdgBlue, Color.White)[index].copy(alpha = 0.22f),
                radius = radius,
                center = orbitCenter,
                style = Stroke(width = 3f)
            )
        }
        val pulsePoint = Offset(
            orbitCenter.x + cos(progress * PI.toFloat() * 2f) * size.minDimension * 0.2f,
            orbitCenter.y + sin(progress * PI.toFloat() * 2f) * size.minDimension * 0.12f
        )
        drawCircle(GdgYellow.copy(alpha = 0.65f), radius = 12f, center = pulsePoint)
    }
}

@Composable
private fun rememberFarewellMotion(): FarewellMotion {
    val transition = rememberInfiniteTransition(label = "jamesFarewell")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(7200, easing = LinearEasing)),
        label = "drift"
    )
    val floaty by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "float"
    )
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )
    val scan by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing)),
        label = "scan"
    )
    val reveal by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5200, easing = LinearEasing), RepeatMode.Reverse),
        label = "reveal"
    )
    val cursor by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(520, easing = LinearEasing), RepeatMode.Reverse),
        label = "cursor"
    )
    val typingProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 8200
                0f at 0
                1f at 4300
                1f at 8200
            }
        ),
        label = "typingProgress"
    )
    val peekTravel by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 7000
                0f at 0
                0f at 4300
                1.08f at 4520
                0.92f at 4660
                1f at 4780
                1f at 5340
                0f at 5580
                0f at 7000
            }
        ),
        label = "peekTravel"
    )
    val peekAlpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 7000
                0f at 0
                0f at 4260
                1f at 4380
                1f at 5380
                0f at 5520
                0f at 7000
            }
        ),
        label = "peekAlpha"
    )
    return FarewellMotion(
        drift = drift,
        floaty = floaty,
        pulse = pulse,
        scan = scan,
        reveal = reveal,
        cursor = cursor,
        typingProgress = typingProgress,
        peekTravel = peekTravel,
        peekAlpha = peekAlpha
    )
}

private data class FarewellMotion(
    val drift: Float,
    val floaty: Float,
    val pulse: Float,
    val scan: Float,
    val reveal: Float,
    val cursor: Float,
    val typingProgress: Float,
    val peekTravel: Float,
    val peekAlpha: Float
)

private val ConsoleDark = Color(0xFF06130C)
private val ConsoleGreen = Color(0xFF0B6E3D)
private val EInk = Color(0xFFDCE5D3)
private val GdgGreen = Color(0xFF0F9D58)
private val GdgYellow = Color(0xFFF4B400)
private val GdgRed = Color(0xFFDB4437)
private val GdgBlue = Color(0xFF4285F4)

private const val FarewellMessage = """
Mario,

I first met you at Droidcon Berlin, at the GDG Berlin Android booth, right when ZeBadge was introduced. It was a fun project to be part of.

I have enjoyed your talks over the years. Although I never quite made it to a GDG Berlin meetup, I know you will be sorely missed.

The fun code project with Marc Reichelt was brilliant too. I still love my copies.

Good luck with everything next. Thanks for the Android community magic.

- James Cullimore
"""
