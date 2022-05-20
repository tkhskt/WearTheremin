package com.tkhskt.theremin.ui.composable

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainUiState

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { dispatcher(MainAction.ClickStartWearableButton) }
        ) {
            Text(text = "Start Wearable")
        }
        Button(
            onClick = { dispatcher(MainAction.ClickCameraButton) }
        ) {
            Text(text = "Start Camera")
        }
        Spacer(modifier = Modifier.size(48.dp))
        Text(text = uiState.frequency.toString(), color = Color.White)
    }
}
