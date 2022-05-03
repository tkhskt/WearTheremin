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
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainUiState

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val state = viewModel.uiState.collectAsState()
    MainScreen(state.value, viewModel::dispatch)
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    dispatcher: (MainAction) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ToggleButton(checked = uiState.started, onCheckedChange = {
            val event = if (it) MainAction.ClickStartButton else MainAction.ClickStopButton
            dispatcher(event)
        }) {
            Text(text = uiState.started.toString())
        }
    }
}
