package de.berlindroid.mario

import android.content.Context
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

object FlutterInitializer {
    fun preWarm(context: Context) {
        val flutterEngine = FlutterEngine(context).apply {
            dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
        }
        FlutterEngineCache.getInstance().put("mario_flutter_engine", flutterEngine)
    }

    fun destroy() {
        FlutterEngineCache.getInstance().get("mario_flutter_engine")?.destroy()
        FlutterEngineCache.getInstance().remove("mario_flutter_engine")
    }
}
