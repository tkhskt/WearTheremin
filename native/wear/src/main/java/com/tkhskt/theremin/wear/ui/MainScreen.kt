package com.tkhskt.theremin.wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.tkhskt.theremin.core.ui.ThereminColorPalette
import com.tkhskt.theremin.wear.theremin.ui.model.MainAction
import com.tkhskt.theremin.wear.theremin.ui.model.MainUiState

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
    Box(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    if (uiState.started) {
                        listOf(ThereminColorPalette.lowVolume2Light, ThereminColorPalette.lowVolume2)
                    } else {
                        listOf(ThereminColorPalette.lowVolume1Light, ThereminColorPalette.lowVolume1)
                    },
                ),
            )
            .clickable {
                val event = if (!uiState.started) MainAction.ClickStartButton else MainAction.ClickStopButton
                dispatcher(event)
            }
            .fillMaxSize(),
    )
}
