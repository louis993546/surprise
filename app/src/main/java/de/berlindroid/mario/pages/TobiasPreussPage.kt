package de.berlindroid.mario.pages

import android.graphics.Paint
import android.media.AudioManager.STREAM_MUSIC
import android.media.ToneGenerator
import android.media.ToneGenerator.TONE_PROP_BEEP
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitVerticalTouchSlopOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.berlindroid.mario.LocalPageIndex
import de.berlindroid.mario.LocalPagerState
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet
import kotlinx.coroutines.isActive

@ContributesIntoSet(AppScope::class)
object TobiasPreussPage : Page {

    override val title = "Tobias Preuẞ Page"
    override val author = "Tobias Preuẞ"

    private var ballX by mutableFloatStateOf(0.5f)
    private var ballY by mutableFloatStateOf(0.5f)
    private var ballVelocityX by mutableFloatStateOf(0.38f)
    private var ballVelocityY by mutableFloatStateOf(0.26f)
    private var leftPaddleY by mutableFloatStateOf(0.5f)
    private var rightPaddleY by mutableFloatStateOf(0.5f)
    private var leftScore by mutableIntStateOf(0)
    private var rightScore by mutableIntStateOf(0)

    @Composable
    override fun LeftContent() {
        val pagerState = LocalPagerState.current
        val pageIndex = LocalPageIndex.current
        val isVisible = pagerState.currentPage == pageIndex
        val hapticFeedback = LocalHapticFeedback.current
        val toneGenerator = remember { ToneGenerator(STREAM_MUSIC, 45) }

        DisposableEffect(toneGenerator) {
            onDispose {
                toneGenerator.release()
            }
        }

        LaunchedEffect(isVisible) {
            if (!isVisible) return@LaunchedEffect

            var previousFrameNanos = withFrameNanos { it }
            while (isActive) {
                val frameNanos = withFrameNanos { it }
                val deltaSeconds = ((frameNanos - previousFrameNanos) / 1_000_000_000f)
                    .coerceAtMost(0.05f)
                previousFrameNanos = frameNanos
                if (updatePong(deltaSeconds)) {
                    hapticFeedback.performHapticFeedback(TextHandleMove)
                    toneGenerator.startTone(TONE_PROP_BEEP, 45)
                }
            }
        }

        PongHalf(isLeft = true)
    }

    @Composable
    override fun RightContent() {
        PongHalf(isLeft = false)
    }

    private fun updatePong(deltaSeconds: Float): Boolean {
        val paddleHeight = 0.22f
        val ballRadius = 0.018f
        val leftPaddleX = 0.06f
        val rightPaddleX = 0.94f
        var hitPaddle = false

        ballX += ballVelocityX * deltaSeconds
        ballY += ballVelocityY * deltaSeconds

        if (ballY <= ballRadius) {
            ballY = ballRadius
            ballVelocityY = kotlin.math.abs(ballVelocityY)
        } else if (ballY >= 1f - ballRadius) {
            ballY = 1f - ballRadius
            ballVelocityY = -kotlin.math.abs(ballVelocityY)
        }

        if (
            ballVelocityX < 0f &&
            ballX <= leftPaddleX + ballRadius &&
            ballY in (leftPaddleY - paddleHeight / 2f)..(leftPaddleY + paddleHeight / 2f)
        ) {
            ballX = leftPaddleX + ballRadius
            ballVelocityX = kotlin.math.abs(ballVelocityX) * 1.02f
            ballVelocityY += (ballY - leftPaddleY) * 0.5f
            hitPaddle = true
        }

        if (
            ballVelocityX > 0f &&
            ballX >= rightPaddleX - ballRadius &&
            ballY in (rightPaddleY - paddleHeight / 2f)..(rightPaddleY + paddleHeight / 2f)
        ) {
            ballX = rightPaddleX - ballRadius
            ballVelocityX = -kotlin.math.abs(ballVelocityX) * 1.02f
            ballVelocityY += (ballY - rightPaddleY) * 0.5f
            hitPaddle = true
        }

        ballVelocityX = ballVelocityX.coerceIn(-0.72f, 0.72f)
        ballVelocityY = ballVelocityY.coerceIn(-0.54f, 0.54f)

        when {
            ballX < -ballRadius -> {
                rightScore += 1
                resetBall(direction = 1f)
            }

            ballX > 1f + ballRadius -> {
                leftScore += 1
                resetBall(direction = -1f)
            }
        }

        return hitPaddle
    }

    private fun resetBall(direction: Float) {
        ballX = 0.5f
        ballY = 0.5f
        ballVelocityX = 0.38f * direction
        ballVelocityY = if ((leftScore + rightScore) % 2 == 0) 0.24f else -0.24f
    }

    @Composable
    internal fun PongHalf(isLeft: Boolean) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PongBlack)
                .pongControls(isLeft),
        ) {
            val image = if (isLeft) R.drawable.timetoplay_left else R.drawable.timetoplay_right
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PongBlack.copy(alpha = 0.42f)),
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawPaddle(isLeft)
                drawBall(isLeft)
                drawScore(isLeft)
            }
        }
    }

    private fun Modifier.pongControls(isLeft: Boolean): Modifier = pointerInput(isLeft) {
        awaitEachGesture {
            val down = awaitPointerEvent().changes.firstOrNull { it.changedToDown() }
                ?: return@awaitEachGesture
            setPaddlePosition(isLeft, down.position.y / size.height)

            var activePointerId: PointerId = down.id
            val drag = awaitVerticalTouchSlopOrCancellation(activePointerId) { change, _ ->
                activePointerId = change.id
                setPaddlePosition(isLeft, change.position.y / size.height)
                change.consume()
            }

            if (drag != null) {
                setPaddlePosition(isLeft, drag.position.y / size.height)
                drag.consume()

                do {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull { it.id == activePointerId }
                    if (change != null) {
                        setPaddlePosition(isLeft, change.position.y / size.height)
                        if (change.positionChange().y != 0f) {
                            change.consume()
                        }
                    }
                } while (change != null && change.pressed)
            }
        }
    }

    private fun setPaddlePosition(isLeft: Boolean, position: Float) {
        val paddleHeight = 0.22f
        val center = position.coerceIn(paddleHeight / 2f, 1f - paddleHeight / 2f)
        if (isLeft) {
            leftPaddleY = center
        } else {
            rightPaddleY = center
        }
    }

    private fun DrawScope.drawPaddle(isLeft: Boolean) {
        val paddleWidth = size.width * 0.022f
        val paddleHeight = size.height * 0.22f
        val paddleInset = size.width * 0.12f
        val paddleCenterY = if (isLeft) leftPaddleY else rightPaddleY
        val paddleX = if (isLeft) paddleInset else size.width - paddleInset - paddleWidth

        drawRect(
            color = PongWhite,
            topLeft = Offset(paddleX, size.height * paddleCenterY - paddleHeight / 2f),
            size = Size(paddleWidth, paddleHeight),
        )
    }

    private fun DrawScope.drawBall(isLeft: Boolean) {
        val isBallOnThisHalf = if (isLeft) ballX <= 0.5f else ballX > 0.5f
        if (!isBallOnThisHalf) return

        val localX = if (isLeft) ballX * 2f else (ballX - 0.5f) * 2f

        drawCircle(
            color = PongWhite,
            radius = minOf(size.width, size.height) * 0.025f,
            center = Offset(size.width * localX, size.height * ballY),
        )
    }

    private fun DrawScope.drawScore(isLeft: Boolean) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = size.minDimension * 0.108f
            alpha = 180
            typeface = android.graphics.Typeface.create(
                android.graphics.Typeface.MONOSPACE,
                android.graphics.Typeface.BOLD,
            )
        }
        val score = if (isLeft) leftScore else rightScore
        val x = if (isLeft) size.width * 0.89f else size.width * 0.11f

        drawContext.canvas.nativeCanvas.drawText(
            score.toString(),
            x,
            size.height * 0.18f,
            paint,
        )
    }

    private val PongBlack = Color(0xFF050505)
    private val PongWhite = Color(0xFFF7F7F0)
}

@Preview
@Composable
private fun TobiasPreussLeftPagePreview() {
    Box(
        modifier = Modifier
            .width(550.dp)
            .height(400.dp),
    ) {
        TobiasPreussPage.PongHalf(isLeft = true)
    }
}

@Preview
@Composable
private fun TobiasPreussRightPagePreview() {
    Box(
        modifier = Modifier
            .width(550.dp)
            .height(400.dp),
    ) {
        TobiasPreussPage.PongHalf(isLeft = false)
    }
}
