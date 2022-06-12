package com.tkhskt.theremin.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.tkhskt.theremin.R
import com.tkhskt.theremin.ui.HandTracker
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.ui.OscillatorController
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import com.tkhskt.theremin.ui.model.MainUiState
import com.tkhskt.theremin.ui.theme.LocalColorPalette
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    oscillatorController: OscillatorController,
    handTracker: HandTracker,
) {
    val state: MainUiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    handTracker.onChangeDistanceListener = { distance: Float ->
        viewModel.dispatch(MainAction.ChangeDistance(distance))
    }
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            launch {
                viewModel.uiState.collect {
                    oscillatorController.run {
                        changeFrequency(state.frequency)
                        changeVolume(state.volume)
                        playSound(state.appSoundEnabled)
                    }
                }
            }
            launch {
                viewModel.sideEffect.collect { effect ->
                    when (effect) {
                        is MainEffect.StartCamera -> {
                            handTracker.startTracking()
                        }
                    }
                }
            }
        }
    }
    MainScreen(state, viewModel::dispatch)
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    dispatcher: (MainAction) -> Unit,
) {
    DrawerScaffold(
        backgroundColor = LocalColorPalette.current.menuBackground,
        mainContentGradientColors = uiState.backgroundGradientColors,
        titleContent = {
            Image(
                modifier = Modifier.height(16.dp),
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
            )
        },
        drawerContent = {
            Menu(
                modifier = Modifier.padding(start = 20.dp),
                pcConnected = uiState.pcConnected,
                watchConnected = uiState.watchConnected,
                appSoundEnabled = uiState.appSoundEnabled,
                browserSoundEnabled = uiState.browserSoundEnabled,
                onClickAppButton = {
                    dispatcher(MainAction.ClickAppSoundButton)
                },
                onClickBrowserButton = {
                    dispatcher(MainAction.ClickBrowserSoundButton)
                },
            )
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            StarryBackground(starCount = 6)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Sun(
                    animate = uiState.appSoundEnabled || uiState.browserSoundEnabled,
                )
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Wave(frequency = uiState.waveGraphicFrequency)
                    Spacer(modifier = Modifier.size(26.5.dp))
                    NoteText(
                        modifier = Modifier.padding(start = 22.dp),
                        note = uiState.note,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    val uiState = MainUiState.Initial.copy(
        waveGraphicFrequency = 10f,
        note = "C#"
    )
    MainScreen(uiState) {}
}
