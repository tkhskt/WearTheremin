package com.tkhskt.theremin.ui.model

import com.tkhskt.theremin.redux.State

data class MainState(
    val frequency: Float,
    val volume: Float,
    val cameraStarted: Boolean,
    val bluetoothInitialized: Boolean,
    val pcConnected: Boolean,
    val watchConnected: Boolean,
    val appSoundEnabled: Boolean,
    val browserSoundEnabled: Boolean,
) : State {
    companion object {
        val INITIAL = MainState(
            frequency = 0f,
            volume = 0f,
            cameraStarted = false,
            bluetoothInitialized = false,
            pcConnected = false,
            watchConnected = false,
            appSoundEnabled = false,
            browserSoundEnabled = false,
        )
    }
}
