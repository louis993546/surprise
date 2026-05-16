package de.berlindroid.mario

import android.app.Application
import de.berlindroid.mario.di.AppGraph
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication

class MarioApplication : Application(), MetroApplication {
    val dependencyGraph by lazy {
        createGraph<AppGraph>()
    }
    override val appComponentProviders: MetroAppComponentProviders
        get() = dependencyGraph
}
