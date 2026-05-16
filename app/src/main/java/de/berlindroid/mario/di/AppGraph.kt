package de.berlindroid.mario.di

import de.berlindroid.mario.model.Page
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metrox.android.MetroAppComponentProviders

abstract class AppScope private constructor()

@DependencyGraph(scope = AppScope::class)
interface AppGraph : MetroAppComponentProviders {
    @get:Multibinds(allowEmpty = true)
    val pages: Set<Page>
}
