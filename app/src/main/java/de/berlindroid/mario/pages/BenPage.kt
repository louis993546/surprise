package de.berlindroid.mario.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet

@ContributesIntoSet(AppScope::class)
object BenPage : Page {

    override val title = "Ben Page"
    override val author = "Ben Kadel"
    val message = """
        Mate you are the heart and soul of the community!
        
        Thank you for all you do & who you are!
        
        You have done an absolutely outstanding job & we are all lucky to know you and to call you friend!
        
        Much love, Ben Kadel
    """.trimIndent()

    @Composable
    override fun LeftContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Blue,
                            Color.Red
                        ),
                        center = Offset(500f, 500f)
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedText(
                text = message,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp,
                maxFontSize = 80.sp
            )

        }
    }

    @Composable
    override fun RightContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Blue,
                            Color.Red
                        ),
                        center = Offset(500f, 500f)
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = R.drawable.mario,
                contentDescription = "Ellie",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    outlineColor: Color = Color.Black,
    strokeWidth: Dp = 3.dp,
    minFontSize: TextUnit = 8.sp,
    maxFontSize: TextUnit = 80.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Center,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        val widthPx = with(density) { maxWidth.roundToPx() }
        val heightPx = with(density) { maxHeight.roundToPx() }
        val strokePx = with(density) { strokeWidth.toPx() }
        val paddingPx = (strokePx * 2).toInt()

        val availableWidth = (widthPx - paddingPx * 2).coerceAtLeast(1)
        val availableHeight = (heightPx - paddingPx * 2).coerceAtLeast(1)

        val fittedFontSize = remember(text, widthPx, heightPx, strokeWidth) {
            var low = minFontSize.value
            var high = maxFontSize.value
            var best = minFontSize.value

            repeat(20) {
                val mid = (low + high) / 2f

                val layout = textMeasurer.measure(
                    text = text,
                    style = TextStyle(
                        fontSize = mid.sp,
                        lineHeight = (mid * 1.05f).sp,
                        fontWeight = fontWeight,
                        textAlign = textAlign
                    ),
                    constraints = Constraints(maxWidth = availableWidth),
                    overflow = TextOverflow.Clip
                )

                if (
                    !layout.hasVisualOverflow &&
                    layout.size.width <= availableWidth &&
                    layout.size.height <= availableHeight
                ) {
                    best = mid
                    low = mid
                } else {
                    high = mid
                }
            }

            best.sp
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val layout = textMeasurer.measure(
                text = text,
                style = TextStyle(
                    fontSize = fittedFontSize,
                    lineHeight = fittedFontSize * 1.05f,
                    fontWeight = fontWeight,
                    textAlign = textAlign
                ),
                constraints = Constraints(maxWidth = availableWidth),
                overflow = TextOverflow.Clip
            )

            val topLeft = Offset(
                x = paddingPx + ((availableWidth - layout.size.width) / 2f),
                y = paddingPx + ((availableHeight - layout.size.height) / 2f)
            )

            drawText(
                textLayoutResult = layout,
                color = outlineColor,
                topLeft = topLeft,
                drawStyle = Stroke(width = strokePx)
            )

            drawText(
                textLayoutResult = layout,
                color = color,
                topLeft = topLeft,
                drawStyle = Fill
            )
        }
    }
}

@Composable
fun OutlinedTextO(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 32.sp,
    textColor: Color = Color.White,
    outlineColor: Color = Color.Black,
    outlineWidth: Float = 8f,
    fontWeight: FontWeight = FontWeight.Normal
) {
    val density = LocalDensity.current

    Canvas(
        modifier = modifier.wrapContentSize()
    ) {
        val textSizePx = with(density) { fontSize.toPx() }

        val strokePaint = android.graphics.Paint().apply {
            isAntiAlias = true
            color = outlineColor.toArgb()
            textSize = textSizePx
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = outlineWidth
            textAlign = android.graphics.Paint.Align.LEFT
        }

        val fillPaint = android.graphics.Paint().apply {
            isAntiAlias = true
            color = textColor.toArgb()
            textSize = textSizePx
            style = android.graphics.Paint.Style.FILL
            textAlign = android.graphics.Paint.Align.LEFT
        }

        val baseline = -strokePaint.fontMetrics.ascent

        drawContext.canvas.nativeCanvas.apply {
            drawText(text, 0f, baseline, strokePaint)
            drawText(text, 0f, baseline, fillPaint)
        }
    }
}

@Preview
@Composable
private fun BensLPagePreview() {
    Box(
        modifier = Modifier
            .width(550.dp)
            .height(400.dp)
            .background(Color(0xFF2C1E11)) // Dark wood-like background for the "table"
    ) {
        BenPage.LeftContent()
    }
}

@Preview
@Composable
private fun BensRPagePreview() {
    Box(
        modifier = Modifier
            .width(550.dp)
            .height(400.dp)
            .background(Color(0xFF2C1E11)) // Dark wood-like background for the "table"
    ) {
        BenPage.RightContent()
    }
}
