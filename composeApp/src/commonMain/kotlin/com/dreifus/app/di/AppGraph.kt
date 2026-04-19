package com.dreifus.app.di

import com.dreifus.app.data.notes.db.DatabaseDriverFactory
import com.dreifus.navigation.controller.NavControllersHolder
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(AppScope::class)
interface AppGraph : ViewModelGraph {

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides driverFactory: DatabaseDriverFactory,
            @Provides navControllersHolder: NavControllersHolder,
        ): AppGraph
    }
}
