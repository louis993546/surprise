package de.berlindroid.mario

import android.app.Application
import de.berlindroid.mario.di.AppGraph
import dev.zacsweers.metro.createGraph
import android.content.pm.ApplicationInfo
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication
import timber.log.Timber

class MarioApplication : Application(), MetroApplication {
    val dependencyGraph by lazy {
        createGraph<AppGraph>()
    }

    override fun onCreate() {
        super.onCreate()
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (isDebuggable) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override val appComponentProviders: MetroAppComponentProviders
        get() = dependencyGraph
}
