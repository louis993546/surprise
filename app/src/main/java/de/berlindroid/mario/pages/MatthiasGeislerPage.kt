package de.berlindroid.mario.pages

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import de.berlindroid.mario.model.PageCategory
import dev.zacsweers.metro.ContributesIntoSet
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private val Sky = Color(0xFF5C94FC)
private val SkyDeep = Color(0xFF3A6AC8)
private val Night = Color(0xFF0B1B4D)
private val Black = Color(0xFF000000)
private val Snow = Color(0xFFFCFCFC)
private val Hero = Color(0xFFE52521)
private val Skin = Color(0xFFF8B878)
private val Brown = Color(0xFF6A3403)
private val Overall = Color(0xFF0058F8)
private val Button = Color(0xFFFAC000)
private val Gold = Color(0xFFFAC000)
private val GoldDark = Color(0xFFB85C00)
private val CoinYellow = Color(0xFFFCD800)
private val Brick = Color(0xFFC84C0C)
private val BrickMortar = Color(0xFF5A1E00)
private val Grass = Color(0xFF00A800)

private val MUSHROOM = listOf(
    "...KKKKKK...",
    "..KRRWWRRK..",
    ".KRRWWWWRRK.",
    "KRWWRRRRWWRK",
    "KRWRRRRRRWRK",
    "KRRRRRRRRRRK",
    ".KKSSSSSSKK.",
    "KKSWWSSWWSKK",
    "KSWWKSSKWWSK",
    "KSWWKSSKWWSK",
    ".KSSSSSSSSK.",
    "..KKKKKKKK..",
)
private val MUSHROOM_PALETTE = mapOf(
    'K' to Black, 'R' to Hero, 'W' to Snow, 'S' to Skin,
)

private val PLAYER = listOf(
    "....HHHHH...",
    "...HHHHHHHH.",
    "...MMMFFKF..",
    "..MFMFFFKFF.",
    "..MFMMFFFKFF",
    "..MMFFFFKKKK",
    "....FFFFFF..",
    "...HHLHHH...",
    "..HHHLHHLHH.",
    ".HHHHLLLLHHH",
    ".FFHLYLLYLHF",
    ".FFFLLLLLLFF",
    ".FFLLLLLLLFF",
    "...LLL..LLL.",
    "..MMM...MMM.",
    ".MMMM...MMMM",
)
private val PLAYER_PALETTE = mapOf(
    'H' to Hero, 'F' to Skin, 'M' to Brown, 'L' to Overall, 'Y' to Button, 'K' to Black,
)

private val COIN = listOf(
    "..KKKK..",
    ".KYYYYK.",
    "KYYOOYYK",
    "KYYOOYYK",
    "KYYOOYYK",
    "KYYOOYYK",
    ".KYYYYK.",
    "..KKKK..",
)
private val COIN_PALETTE = mapOf(
    'K' to Black, 'Y' to CoinYellow, 'O' to GoldDark,
)

private val BLOCK = listOf(
    "KKKKKKKKKKKK",
    "KGGGGGGGGGGK",
    "KGDGGGGGGDGK",
    "KGGGGGGGGGGK",
    "KGGGGGGGGGGK",
    "KGGGGGGGGGGK",
    "KGGGGGGGGGGK",
    "KGGGGGGGGGGK",
    "KGGGGGGGGGGK",
    "KGDGGGGGGDGK",
    "KGGGGGGGGGGK",
    "KKKKKKKKKKKK",
)
private val BLOCK_PALETTE = mapOf(
    'K' to Black, 'G' to Gold, 'D' to GoldDark,
)

private const val MAIN_TEXT = "Alles hat ein Ende nur die Tofuwurst hat zwei und dies ist sicherlich kein Poesiealbum Eintrag. Auch wenn du 11 Jahre bis ins nächste Level gebrauchst hast…geschafft hast du es trotzdem. Just kidding. \n" +
        "Ich glaube, dass du selbst weißt, dass nichts, was ich hier schreibe, auch nur ansatzweise den Impact beschreiben könnte, den du auf die Community und damit auf die Leute hattest.(Und ein Danke dafür)\n" +
        "Ich hoffe, dass du trotz des Levelups dennoch noch manchmal mit Wehmut an uns denkst. \n"

@ContributesIntoSet(AppScope::class)
object MatthiasGeislerPage : Page {
    override val title: String = "Itsa me"
    override val author: String = "Matthias Geisler"
    override val category: PageCategory = PageCategory.Community

    @Composable
    override fun LeftContent() = TitleScreen()

    @Composable
    override fun RightContent() = LevelComplete()
}

@Composable
private fun PixelSprite(
    rows: List<String>,
    palette: Map<Char, Color>,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier) {
        val cols = rows.maxOf { it.length }
        val pw = size.width / cols
        val ph = size.height / rows.size
        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                val color = palette[c] ?: return@forEachIndexed
                drawRect(
                    color = color,
                    topLeft = Offset(x * pw, y * ph),
                    size = Size(pw + 0.7f, ph + 0.7f),
                )
            }
        }
    }
}

@Composable
private fun OutlinedPixelText(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    color: Color,
    modifier: Modifier = Modifier,
    letterSpacing: androidx.compose.ui.unit.TextUnit = 2.sp,
) {
    Box(modifier) {
        Text(
            text = text,
            color = Black,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            modifier = Modifier.offset(x = 3.dp, y = 3.dp),
        )
        Text(
            text = text,
            color = color,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
        )
    }
}

private fun DrawScope.pixelBricks(topY: Float, tile: Float) {
    drawRect(Brick, topLeft = Offset(0f, topY), size = Size(size.width, size.height - topY))
    drawRect(Grass, topLeft = Offset(0f, topY), size = Size(size.width, tile * 0.35f))
    drawRect(Black, topLeft = Offset(0f, topY + tile * 0.35f), size = Size(size.width, tile * 0.12f))
    var row = 0
    var y = topY + tile * 0.47f
    while (y < size.height) {
        drawRect(BrickMortar, topLeft = Offset(0f, y), size = Size(size.width, tile * 0.12f))
        val offset = if (row % 2 == 0) 0f else tile / 2f
        var x = offset - tile
        while (x < size.width) {
            drawRect(BrickMortar, topLeft = Offset(x, y), size = Size(tile * 0.12f, tile))
            x += tile
        }
        y += tile
        row++
    }
}

private fun DrawScope.pixelCloud(cx: Float, cy: Float, unit: Float) {
    fun blk(gx: Int, gy: Int, w: Int = 1, h: Int = 1) = drawRect(
        Snow,
        topLeft = Offset(cx + gx * unit, cy + gy * unit),
        size = Size(w * unit + 0.7f, h * unit + 0.7f),
    )
    blk(2, 0, 2); blk(1, 1, 4); blk(0, 2, 7, 1)
}

@Composable
private fun TitleScreen() {
    val t = rememberInfiniteTransition(label = "title")
    val bob by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(850, easing = LinearEasing), RepeatMode.Reverse),
        label = "bob",
    )
    val blink by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(560, easing = LinearEasing), RepeatMode.Reverse),
        label = "blink",
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Sky, SkyDeep))),
    ) {
        val w = maxWidth.value
        val titleSize = (w / 7f).coerceIn(28f, 96f).sp
        val subSize = (w / 18f).coerceIn(12f, 34f).sp
        val tile = (maxWidth.value * 1.6f).coerceIn(64f, 150f)

        Canvas(modifier = Modifier.fillMaxSize()) {
            val unit = (size.width / 70f)
            pixelCloud(size.width * 0.12f, size.height * 0.14f, unit)
            pixelCloud(size.width * 0.66f, size.height * 0.24f, unit * 1.3f)
            pixelBricks(size.height * 0.84f, tile)
            var y = 0f
            while (y < size.height) {
                drawRect(Black.copy(alpha = 0.06f), topLeft = Offset(0f, y), size = Size(size.width, 1.6f))
                y += 5f
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))
            OutlinedPixelText("THANK YOU", subSize, Button)
            Spacer(Modifier.height(8.dp))
            OutlinedPixelText("MARIO!", titleSize, Snow)

            Spacer(Modifier.weight(1f))

            PixelSprite(
                rows = MUSHROOM,
                palette = MUSHROOM_PALETTE,
                modifier = Modifier
                    .size((w / 6f).coerceIn(72f, 170f).dp)
                    .offset(y = (-14f * bob).dp),
            )

            Spacer(Modifier.weight(1f))

            if (blink > 0.45f) {
                OutlinedPixelText("> PRESS START", subSize, Snow, letterSpacing = 1.sp)
            } else {
                Spacer(Modifier.height(subSize.value.dp + 6.dp))
            }
            Spacer(Modifier.height(28.dp))
        }

        Text(
            text = "PLAYER 1   ©2015 BERLINDROID",
            color = Snow.copy(alpha = 0.7f),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
        )
    }
}

@Composable
private fun LevelComplete() {
    val t = rememberInfiniteTransition(label = "level")
    val jump by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Reverse),
        label = "jump",
    )
    val spin by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(1400, easing = LinearEasing)),
        label = "spin",
    )
    val flag by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse),
        label = "flag",
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SkyDeep, Night))),
    ) {
        val w = maxWidth.value
        val h = maxHeight.value
        val tile = (maxWidth.value * 1.6f).coerceIn(64f, 150f)

        Canvas(modifier = Modifier.fillMaxSize()) {
            val unit = (size.width / 70f)
            pixelCloud(size.width * 0.18f, size.height * 0.10f, unit * 1.1f)
            pixelBricks(size.height * 0.82f, tile)

            val poleX = size.width * 0.84f
            val groundY = size.height * 0.82f
            val poleTop = size.height * 0.30f
            drawRect(Color(0xFFB8B8B8), topLeft = Offset(poleX, poleTop), size = Size(6f, groundY - poleTop))
            drawRect(Grass, topLeft = Offset(poleX - 7f, poleTop - 12f), size = Size(20f, 20f))
            val flagY = poleTop + (groundY - poleTop) * (0.10f + flag * 0.10f)
            val fw = size.width * 0.08f
            drawRect(Grass, topLeft = Offset(poleX - fw, flagY), size = Size(fw, fw * 0.7f))

            // CRT scanlines
            var y = 0f
            while (y < size.height) {
                drawRect(Black.copy(alpha = 0.06f), topLeft = Offset(0f, y), size = Size(size.width, 1.6f))
                y += 5f
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // HUD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("MARIO x ∞", color = Snow, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("WORLD 1-1", color = Snow, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("TIME ∞", color = Snow, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(Modifier.height(18.dp))
            OutlinedPixelText("LEVEL COMPLETE!", (w / 17f).coerceIn(14f, 32f).sp, Button)
            Spacer(Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QuestionBlock(Modifier.size((w / 9f).coerceIn(44f, 96f).dp))
                SpinningCoin(spin, Modifier.size((w / 12f).coerceIn(32f, 70f).dp))
                QuestionBlock(Modifier.size((w / 9f).coerceIn(44f, 96f).dp))
                SpinningCoin(spin + 0.3f, Modifier.size((w / 12f).coerceIn(32f, 70f).dp))
                QuestionBlock(Modifier.size((w / 9f).coerceIn(44f, 96f).dp))
            }

            Spacer(Modifier.height(10.dp))

            PixelSprite(
                rows = PLAYER,
                palette = PLAYER_PALETTE,
                modifier = Modifier
                    .size((w / 6.5f).coerceIn(64f, 150f).dp)
                    .offset(y = (-40f * abs(sin(jump * Math.PI.toFloat()))).dp),
            )

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = (h * 0.42f).dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                    .background(Black.copy(alpha = 0.55f))
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = MAIN_TEXT,
                    color = Snow,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(weight = 1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                )
                Spacer(Modifier.height(10.dp))
                // Pinned below the scroll area so it stays visible.
                Text(
                    text = "+ 1 UP   *  THANK YOU  *",
                    color = CoinYellow,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                )
            }
        }

        Text(
            text = "by Matthias Geisler",
            color = Snow.copy(alpha = 0.6f),
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
        )
    }
}

@Composable
private fun QuestionBlock(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        PixelSprite(BLOCK, BLOCK_PALETTE, Modifier.fillMaxSize())
        Text(
            text = "?",
            color = Brown,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            fontSize = 26.sp,
        )
    }
}

@Composable
private fun SpinningCoin(progress: Float, modifier: Modifier = Modifier) {
    PixelSprite(
        rows = COIN,
        palette = COIN_PALETTE,
        modifier = modifier.graphicsLayer {
            scaleX = cos(progress * 2f * Math.PI.toFloat()).coerceAtLeast(0.12f)
        },
    )
}
