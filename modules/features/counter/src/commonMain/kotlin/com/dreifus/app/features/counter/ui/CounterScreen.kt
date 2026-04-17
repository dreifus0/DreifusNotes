package com.dreifus.app.features.counter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreifus.app.features.counter.CounterViewModel
import com.dreifus.app.features.counter.mvu.CounterEffect
import com.dreifus.app.features.counter.mvu.CounterEvent
import com.dreifus.arch.lce.LceState
import com.dreifus.arch.lce.isLoading
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.ui.RootScreenWithTabs
import dev.zacsweers.metrox.viewmodel.metroViewModel
import com.dreifus.template.uikit.style.AppTheme

class CounterScreen : RootScreenWithTabs {

    @Composable
    override fun Content() {
        val viewModel = metroViewModel<CounterViewModel>()
        val store = viewModel.store

        val state by store.state.collectAsState()
        val nav = Navigation.regular
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(store) {
            store.effects.collect { effect ->
                when (effect) {
                    is CounterEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message)
                    is CounterEffect.NavigateToDetail -> nav.navigate(CounterDetailScreen())
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                when (val countState = state.countState) {
                    is LceState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is LceState.Content -> {
                        Text(
                            text = "Count: ${countState.value}",
                            style = AppTheme.typography.headlineLarge,
                            color = AppTheme.colors.contentPrimary,
                        )
                    }

                    is LceState.Error -> {
                        Text(
                            text = "Error: ${countState.error?.message ?: "Unknown error"}",
                            style = AppTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { store.dispatch(CounterEvent.Decrement) }) {
                        Text("-1")
                    }
                    Button(onClick = { store.dispatch(CounterEvent.Increment) }) {
                        Text("+1")
                    }
                    Button(
                        onClick = { store.dispatch(CounterEvent.AsyncIncrement) },
                        enabled = !state.countState.isLoading,
                    ) {
                        Text("Async +1")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { store.dispatch(CounterEvent.OpenDetail) }) {
                    Text("Open Detail")
                }
            }
        }
    }
}
