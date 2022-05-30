package com.tkhskt.theremin.ui.model

import com.tkhskt.theremin.redux.UiState

data class MainUiState(
    val frequency: Float,
    val volume: Float,
    val waveGraphicFrequency: Float,
    val note: String,
    val pcConnected: Boolean,
    val watchConnected: Boolean,
    val appSoundEnabled: Boolean,
    val browserSoundEnabled: Boolean,
) : UiState {
    companion object {
        val Initial = MainUiState(
            frequency = 0f,
            volume = 0f,
            waveGraphicFrequency = 0f,
            note = "",
            pcConnected = false,
            watchConnected = false,
            appSoundEnabled = false,
            browserSoundEnabled = false,
        )
    }
}
