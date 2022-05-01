package com.tkhskt.theremin.ui.model

import com.tkhskt.theremin.redux.State

data class MainState(
    val frequency: Float,
    val volume: Float,
    val cameraStarted: Boolean,
    val bluetoothInitialized: Boolean,
) : State {
    companion object {
        val INITIAL = MainState(
            frequency = 0f,
            volume = 0f,
            cameraStarted = false,
            bluetoothInitialized = false,
        )
    }
}
