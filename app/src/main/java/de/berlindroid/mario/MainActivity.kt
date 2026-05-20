package de.berlindroid.mario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
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
import de.berlindroid.mario.di.AppGraph
import de.berlindroid.mario.ui.flip.FlipPager
import de.berlindroid.mario.ui.flip.FlipPagerOrientation
import de.berlindroid.mario.ui.theme.MarioTheme
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import timber.log.Timber

val LocalAppGraph = staticCompositionLocalOf<AppGraph> {
    error("AppGraph not provided")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Pre-warm Flutter Engine
        val flutterEngine = FlutterEngine(this).apply {
            dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
        }
        FlutterEngineCache.getInstance().put("mario_flutter_engine", flutterEngine)

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

    override fun onDestroy() {
        // Clean up pre-warmed Flutter Engine
        FlutterEngineCache.getInstance().get("mario_flutter_engine")?.destroy()
        FlutterEngineCache.getInstance().remove("mario_flutter_engine")
        super.onDestroy()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppContent() {
    val appGraph = LocalAppGraph.current
    val pages = remember(appGraph) { 
        val list = appGraph.pages.sortedBy { it.category.weight }
        Timber.tag("MarioBook")
            .d("Discovered ${list.size} pages (sorted): ${list.map { "${it.title}(${it.category::class.simpleName})" }}")
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
        FlipPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            orientation = FlipPagerOrientation.Horizontal
        ) { index ->
            val page = pages[index]

            Card(
                modifier = Modifier.fillMaxSize(),
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
