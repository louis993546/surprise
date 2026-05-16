package de.berlindroid.mario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.unit.dp
import de.berlindroid.mario.di.AppGraph
import de.berlindroid.mario.ui.theme.MarioTheme

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
        val list = appGraph.pages.toList()
        android.util.Log.d("MarioBook", "Discovered ${list.size} pages: ${list.map { it.title }}")
        list
    }
    
    if (pages.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("The book is currently empty. Add a @ContributesBinding(AppScope::class) Page!")
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5DC)) // Paper-ish color
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            val page = pages[index]
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Page
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    page.LeftContent()
                }
                
                // Binding/Folding line
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.05f),
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Black.copy(alpha = 0.05f)
                                )
                            )
                        )
                )

                // Right Page
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    page.RightContent()
                }
            }
        }
        
        // Visual "folded corner" indicator
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(80.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.03f)),
                        start = androidx.compose.ui.geometry.Offset.Zero,
                        end = androidx.compose.ui.geometry.Offset.Infinite
                    )
                )
        )
    }
}
