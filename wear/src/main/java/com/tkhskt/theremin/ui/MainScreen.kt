package com.tkhskt.theremin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleButton
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ToggleButton(checked = uiState.started, onCheckedChange = {
            val event = if (it) MainEvent.ClickStartButton else MainEvent.ClickStopButton
            dispatcher(event)
        }) {
            Text(text = uiState.started.toString())
        }
    }
}
