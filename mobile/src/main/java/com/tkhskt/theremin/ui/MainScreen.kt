package com.tkhskt.theremin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.ui.model.MainEvent
import com.tkhskt.theremin.ui.model.MainState

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val state = viewModel.state.collectAsState()
    MainScreen(state.value, viewModel::dispatchEvent)
}

@Composable
fun MainScreen(
    uiState: MainState,
    dispatcher: (MainEvent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { dispatcher(MainEvent.ClickStartWearableButton) }
        ) {
            Text(text = "Start Wearable")
        }
        Button(
            onClick = { dispatcher(MainEvent.ClickCameraButton) }
        ) {
            Text(text = "Start Camera")
        }
        Spacer(modifier = Modifier.size(48.dp))
        Text(text = uiState.frequency, color = Color.White)
    }
}
