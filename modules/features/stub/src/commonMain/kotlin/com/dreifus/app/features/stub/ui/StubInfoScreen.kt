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
import com.dreifus.navigation.screen.regular.RegularScreen
import com.dreifus.template.uikit.style.AppTheme

class StubInfoScreen : RegularScreen {

    @Composable
    override fun Content() {
        val nav = Navigation.regular
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Stub Info",
                style = AppTheme.typography.headlineLarge,
                color = AppTheme.colors.contentPrimary,
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
