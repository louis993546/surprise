package de.berlindroid.mario.pages

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.model.Page
import de.berlindroid.mario.model.PageCategory
import dev.zacsweers.metro.ContributesIntoSet
import io.flutter.embedding.android.ExclusiveAppComponent
import io.flutter.embedding.android.FlutterTextureView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint

@ContributesIntoSet(AppScope::class)
object FlutterDemoPage : Page {
    override val title: String = "Flutter & Compose"
    override val author: String = "Louis & Antigravity"
    override val category: PageCategory = PageCategory.AlmostOutro

    @Composable
    override fun LeftContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5DC))
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "The Compose Page",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E2723),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Pill badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE6F4EA))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Rendered in Jetpack Compose",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF137333)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                // Explanation card
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEFEBE9))
                        .padding(32.dp)
                ) {
                    Text(
                        text = "This side is built natively with Jetpack Compose. It renders directly via native Android graphics, running Kotlin on the JVM. The state is handled by Compose runtime and is completely isolated from the Flutter side.",
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    @Composable
    override fun RightContent() {
        val context = LocalContext.current
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        val activity = remember(context) { context.findActivity() }

        // We retrieve the pre-warmed FlutterEngine from cache, or fall back to creating one
        val flutterEngine = remember(context) {
            FlutterEngineCache.getInstance().get("mario_flutter_engine")
                ?: FlutterEngine(context).apply {
                    dartExecutor.executeDartEntrypoint(
                        DartEntrypoint.createDefault()
                    )
                    FlutterEngineCache.getInstance().put("mario_flutter_engine", this)
                }
        }

        // We remember the FlutterView, making sure to use FlutterTextureView to support the 3D page-flip animation
        val flutterView = remember(context) {
            FlutterView(context, FlutterTextureView(context)).apply {
                attachToFlutterEngine(flutterEngine)
            }
        }

        val exclusiveAppComponent = remember(activity) {
            object : ExclusiveAppComponent<Activity> {
                override fun getAppComponent(): Activity {
                    return activity ?: error("Activity not found")
                }

                override fun detachFromFlutterEngine() {
                    // No-op
                }
            }
        }

        // Coordinate the Flutter engine with the Activity lifecycle and clean up on dispose
        DisposableEffect(flutterEngine, lifecycle, exclusiveAppComponent) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        flutterEngine.lifecycleChannel.appIsResumed()
                        flutterEngine.activityControlSurface.attachToActivity(exclusiveAppComponent, lifecycle)
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        flutterEngine.lifecycleChannel.appIsPaused()
                    }
                    Lifecycle.Event.ON_STOP -> {
                        flutterEngine.lifecycleChannel.appIsInactive()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        flutterEngine.lifecycleChannel.appIsDetached()
                        flutterEngine.activityControlSurface.detachFromActivity()
                    }
                    else -> {}
                }
            }

            // Also trigger initial attach/resume if the host lifecycle is already resumed
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                flutterEngine.lifecycleChannel.appIsResumed()
                flutterEngine.activityControlSurface.attachToActivity(exclusiveAppComponent, lifecycle)
            } else if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                flutterEngine.activityControlSurface.attachToActivity(exclusiveAppComponent, lifecycle)
            }

            lifecycle.addObserver(observer)

            onDispose {
                lifecycle.removeObserver(observer)
                flutterView.detachFromFlutterEngine()
                flutterEngine.activityControlSurface.detachFromActivity()
                // Note: We do not destroy the engine here because it is pre-warmed and managed at the Activity level!
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5DC)),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { flutterView },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    private tailrec fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
