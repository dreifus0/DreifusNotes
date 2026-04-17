package com.dreifus.app.di

import androidx.lifecycle.ViewModel
import com.dreifus.app.features.notes.main.NotesListScreen
import com.dreifus.app.features.stub.ui.StubScreen
import com.dreifus.navigation.controller.NavControllersHolder
import com.dreifus.navigation.controller.TabNavState
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

class RootViewModel : ViewModel() {
    private val graph = createGraphFactory<AppGraph.Factory>()
        .create(createDatabaseDriverFactory())
    val factory: MetroViewModelFactory = graph.metroViewModelFactory

    val navControllersHolder = NavControllersHolder(
        tabNavState = TabNavState(
            tabRoots = listOf(NotesListScreen(), StubScreen()),
            initialActiveIndex = 0,
        ),
    )
}
