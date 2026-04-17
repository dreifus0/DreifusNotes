package com.dreifus.navigation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreifus.navigation.screen.bottomsheet.BottomSheetScreen
import com.dreifus.template.uikit.dialog.BottomSheetHeader
import com.dreifus.template.uikit.style.AppTheme

class StubBottomSheetScreen(val title: String) : BottomSheetScreen {

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            BottomSheetHeader(
                title = title,
                paddingTop = 48.dp,
                paddingBottom = 0.dp
            )
            Text(
                text = title,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.contentPrimary,
            )
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}
