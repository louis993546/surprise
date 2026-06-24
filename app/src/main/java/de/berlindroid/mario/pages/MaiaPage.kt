package de.berlindroid.mario.pages

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.ContributesIntoSet
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

data class Bubble(
    val id: Int,
    val xFraction: Float,
    val yFraction: Float,
    val radiusDp: Float,
    val color: Color,
    val isPopped: Boolean = false
)

@ContributesIntoSet(AppScope::class)
object MaiaPage : Page {

    private val fontFamily = FontFamily(
        Font(R.font.caveat_variable_font_wght)
    )

    override val title: String = "Maia's Message"
    override val author: String = "Maia"

    private fun generateRandomBubbles(): List<Bubble> {
        val colors = listOf(
            Color(0xBB00E5FF), // Vibrant Cyan
            Color(0xBBD500F9), // Vibrant Purple
            Color(0xBBFF4081), // Vibrant Pink
            Color(0xBBFFAB00), // Vibrant Orange
            Color(0xBB00E676), // Vibrant Green
            Color(0xBBFFEA00), // Vibrant Yellow
            Color(0xBB2979FF), // Vibrant Blue
            Color(0xBBFF3D00)  // Vibrant Red/Orange
        )
        return List(120) { id ->
            Bubble(
                id = id,
                xFraction = kotlin.random.Random.nextFloat(),
                yFraction = 0.05f + kotlin.random.Random.nextFloat() * 0.8f,
                radiusDp = 25f + kotlin.random.Random.nextFloat() * 55f, // Sizes 25dp to 80dp
                color = colors.random()
            )
        }
    }

    @Composable
    override fun LeftContent() {
        var bubbles by remember { mutableStateOf(generateRandomBubbles()) }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            val width = maxWidth
            val height = maxHeight

            // 1. Secret message underneath the bubbles
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Dear Mario,\n\nyou will be missed but I am sure you will have a huge impact on all your future projects.\n\nThanks for everything,\nMaia",
                        fontFamily = fontFamily,
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center,
                        lineHeight = MaterialTheme.typography.displayMedium.lineHeight * 1.2
                    )
                }
            }

            // 2. Interactive Bubbles layer
            bubbles.forEach { bubble ->
                val scale by animateFloatAsState(
                    targetValue = if (bubble.isPopped) 0f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "bubble_scale"
                )

                if (scale > 0.01f) {
                    val x = width * bubble.xFraction
                    val y = height * bubble.yFraction
                    val size = (bubble.radiusDp * 2 * scale).dp

                    Box(
                        modifier = Modifier
                            .offset(
                                x = x - (bubble.radiusDp * scale).dp,
                                y = y - (bubble.radiusDp * scale).dp
                            )
                            .size(size)
                            .clip(CircleShape)
                            .background(bubble.color)
                            .clickable(enabled = !bubble.isPopped) {
                                bubbles = bubbles.map {
                                    if (it.id == bubble.id) it.copy(isPopped = true) else it
                                }
                            }
                    )
                }
            }

            // 3. Control Buttons at the bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        bubbles = bubbles.map { it.copy(isPopped = true) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Pop All",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        bubbles = generateRandomBubbles()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    private fun countNeighbors(row: Int, col: Int, size: Int, liveCells: Set<Pair<Int, Int>>): Int {
        var count = 0
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val r = (row + dr + size) % size
                val c = (col + dc + size) % size
                if (liveCells.contains(r to c)) {
                    count++
                }
            }
        }
        return count
    }

    private fun nextGeneration(currentCells: Set<Pair<Int, Int>>, size: Int): Set<Pair<Int, Int>> {
        val next = mutableSetOf<Pair<Int, Int>>()
        for (r in 0 until size) {
            for (c in 0 until size) {
                val neighbors = countNeighbors(r, c, size, currentCells)
                val isAlive = currentCells.contains(r to c)
                if (isAlive) {
                    if (neighbors == 2 || neighbors == 3) {
                        next.add(r to c)
                    }
                } else {
                    if (neighbors == 3) {
                        next.add(r to c)
                    }
                }
            }
        }
        return next
    }

    private fun randomizeGrid(size: Int): Set<Pair<Int, Int>> {
        val randomCells = mutableSetOf<Pair<Int, Int>>()
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (kotlin.random.Random.nextFloat() < 0.25f) { // 25% fill rate
                    randomCells.add(r to c)
                }
            }
        }
        return randomCells
    }

    @Composable
    override fun RightContent() {
        var gridSize by remember { mutableIntStateOf(15) }
        var liveCells by remember {
            mutableStateOf(
                setOf(
                    1 to 2,
                    2 to 3,
                    3 to 1,
                    3 to 2,
                    3 to 3
                )
            )
        }
        var isRunning by remember { mutableStateOf(false) }

        LaunchedEffect(isRunning, gridSize) {
            if (isRunning) {
                while (isRunning) {
                    kotlinx.coroutines.delay(250.milliseconds)
                    val next = nextGeneration(liveCells, gridSize)
                    if (next == liveCells || next.isEmpty()) {
                        liveCells = next
                        isRunning = false
                    } else {
                        liveCells = next
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Header: Displays "Life goes on" when running
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isRunning) {
                    Text(
                        text = "Life goes on",
                        fontFamily = fontFamily,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 2. Interactive Grid
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (r in 0 until gridSize) {
                        Row(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            for (c in 0 until gridSize) {
                                val isAlive = liveCells.contains(r to c)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            if (isAlive) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                        )
                                        .clickable {
                                            if (!isRunning) {
                                                liveCells = if (isAlive) liveCells - (r to c) else liveCells + (r to c)
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }

            // Increased padding between the grid simulator and the slider
            Spacer(modifier = Modifier.height(32.dp))

            // 3. Grid Size Slider
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Size: ${gridSize}x${gridSize}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.width(110.dp)
                )
                Slider(
                    value = gridSize.toFloat(),
                    onValueChange = {
                        gridSize = it.roundToInt()
                        liveCells = liveCells.filter { cell ->
                            cell.first < gridSize && cell.second < gridSize
                        }.toSet()
                    },
                    valueRange = 8f..25f,
                    enabled = !isRunning,
                    modifier = Modifier.weight(1f)
                )
            }

            // 4. Controls Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { isRunning = !isRunning },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) Color(0xFFE53935) else Color(0xFF43A047)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(110.dp)
                ) {
                    Text(
                        text = if (isRunning) "Stop" else "Run",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Button(
                    onClick = {
                        liveCells = emptySet()
                        isRunning = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(110.dp)
                ) {
                    Text(
                        text = "Clear",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Button(
                    onClick = {
                        liveCells = randomizeGrid(gridSize)
                    },
                    enabled = !isRunning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(110.dp)
                ) {
                    Text(
                        text = "Random",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            // 5. Instructions moved below the buttons
            Text(
                text = "Tap cells to design. Toggle Run to see it evolve.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
