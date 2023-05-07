package com.tkhskt.theremin.feature.theremin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminEffect
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ThereminRoute(
    viewModel: ThereminViewModel,
    oscillatorController: OscillatorController,
    handTracker: HandTracker,
    navigateToLicense: () -> Unit,
) {
    val state: ThereminUiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        handTracker.onChangeDistanceListener = { distance: Float ->
            viewModel.dispatch(ThereminAction.ChangeDistance(distance))
        }
    }
    LaunchedEffect(
        state.volume,
        state.frequency,
        state.appSoundEnabled,
    ) {
        oscillatorController.run {
            changeFrequency(state.frequency)
            changeVolume(state.volume)
            if (state.appSoundEnabled) {
                play()
            } else {
                pause()
            }

        }
    }
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.sideEffect.onEach { effect ->
                when (effect) {
                    is ThereminEffect.StartCamera -> {
                        handTracker.startTracking()
                    }

                    is ThereminEffect.NavigateToLicense -> {
                        navigateToLicense()
                    }
                }
            }.launchIn(this)
        }
    }
    ThereminScreen(
        uiState = state,
        onClickAppButton = {
            viewModel.dispatch(ThereminAction.ClickAppSoundButton)
        },
        onClickLicense = {
            viewModel.dispatch(ThereminAction.ClickLicenseButton)
        },
    )
}
