package com.dreifus.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dreifus.app.di.RootViewModel
import com.dreifus.app.navigation.mainTabs
import com.dreifus.navigation.NavigationSetup
import com.dreifus.navigation.ui.LocalTabs
import com.dreifus.template.uikit.style.AppTheme
import com.dreifus.template.uikit.style.app.DefaultAppTheme
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

@Composable
fun App() {
    val rootViewModel = viewModel { RootViewModel() }

    DefaultAppTheme(darkTheme = isSystemInDarkTheme()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.colors.backgroundBase
        ) {
            CompositionLocalProvider(
                LocalTabs provides mainTabs,
                LocalMetroViewModelFactory provides rootViewModel.factory,
            ) {
                NavigationSetup(
                    navControllersHolder = rootViewModel.navControllersHolder,
                    listener = { screen ->
                        println("Navigation: $screen")
                    },
                )
            }
        }
    }
}
