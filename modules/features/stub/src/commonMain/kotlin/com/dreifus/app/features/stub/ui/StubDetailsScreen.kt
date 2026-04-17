package com.dreifus.app.features.stub.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreifus.navigation.controller.Navigation
import com.dreifus.navigation.ui.RootScreenWithTabs
import com.dreifus.template.uikit.style.AppTheme

class StubDetailsScreen : RootScreenWithTabs {

    @Composable
    override fun Content() {
        val nav = Navigation.regular
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Stub Details",
                style = AppTheme.typography.headlineLarge,
                color = AppTheme.colors.contentPrimary,
            )
            Text(
                text = "Tab bar is still visible!",
                modifier = Modifier.padding(top = 8.dp),
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.contentSecondary,
            )
            Button(
                modifier = Modifier.padding(top = 24.dp),
                onClick = { nav.pop() },
            ) {
                Text("Back")
            }
        }
    }
}
