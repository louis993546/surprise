package de.berlindroid.mario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import de.berlindroid.mario.di.AppGraph
import de.berlindroid.mario.ui.theme.MarioTheme
import de.berlindroid.mario.MarioApplication
import kotlin.math.absoluteValue

val LocalAppGraph = staticCompositionLocalOf<AppGraph> {
    error("AppGraph not provided")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appGraph = (application as MarioApplication).dependencyGraph
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalAppGraph provides appGraph) {
                MarioTheme {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    val appGraph = LocalAppGraph.current
    val pages = remember(appGraph) { 
        val list = appGraph.pages.sortedBy { it.order }
        android.util.Log.d("MarioBook", "Discovered ${list.size} pages (sorted): ${list.map { "${it.title}(${it.order})" }}")
        list
    }
    
    if (pages.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("The book is currently empty. Add a @ContributesIntoSet(AppScope::class) Page!")
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C1E11)) // Dark wood-like background for the "table"
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 0.dp,
            beyondViewportPageCount = 1
        ) { index ->
            val page = pages[index]
            
            // Page Flip Physics Simulation using GraphicsLayer
            val pageOffset = (
                (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
            )
            
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // 3D rotation effect
                        rotationY = lerp(
                            start = 0f,
                            stop = -180f,
                            fraction = pageOffset.coerceIn(-1f, 1f)
                        )
                        
                        // Pivot on the center binding line
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(
                            pivotFractionX = if (pageOffset > 0) 1f else 0f,
                            pivotFractionY = 0.5f
                        )
                        
                        // Hide backface when flipped
                        alpha = if (pageOffset.absoluteValue > 0.5f) 0f else 1f
                        
                        // Scale slightly to simulate depth
                        val scale = lerp(1f, 0.95f, pageOffset.absoluteValue)
                        scaleX = scale
                        scaleY = scale
                    },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5DC))
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Left Page
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            page.LeftContent()
                        }
                        
                        // Center Binding Shadow (Deep gutter)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(8.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.15f),
                                            Color.Black.copy(alpha = 0.4f),
                                            Color.Black.copy(alpha = 0.15f)
                                        )
                                    )
                                )
                        )

                        // Right Page
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            page.RightContent()
                        }
                    }
                    
                    // Realistic Paper Texture/Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    0.0f to Color.Black.copy(alpha = 0.05f),
                                    0.1f to Color.Transparent,
                                    0.45f to Color.Transparent,
                                    0.5f to Color.Black.copy(alpha = 0.1f),
                                    0.55f to Color.Transparent,
                                    0.9f to Color.Transparent,
                                    1.0f to Color.Black.copy(alpha = 0.05f)
                                )
                            )
                    )
                    
                    // Folded Corner (Dog-ear) Visual
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                            .graphicsLayer {
                                rotationZ = -45f
                                translationX = 20f
                                translationY = 20f
                            }
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.1f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}
