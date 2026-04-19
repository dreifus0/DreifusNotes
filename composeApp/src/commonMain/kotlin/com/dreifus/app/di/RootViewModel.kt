package com.dreifus.app.di

import androidx.lifecycle.ViewModel
import com.dreifus.app.features.notes.main.NotesListScreen
import com.dreifus.app.features.settings.main.SettingsScreen
import com.dreifus.navigation.controller.NavControllersHolder
import com.dreifus.navigation.controller.TabNavState
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

class RootViewModel : ViewModel() {
    val navControllersHolder = NavControllersHolder(
        tabNavState = TabNavState(
            tabRoots = listOf(NotesListScreen(), SettingsScreen()),
            initialActiveIndex = 0,
        ),
    )

    private val graph = createGraphFactory<AppGraph.Factory>()
        .create(createDatabaseDriverFactory(), navControllersHolder)
    val factory: MetroViewModelFactory = graph.metroViewModelFactory
}
