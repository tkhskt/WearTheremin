package com.tkhskt.theremin.feature.theremin.ui.model

import androidx.compose.ui.graphics.Color
import com.tkhskt.theremin.core.ui.ThereminColorPalette
import com.tkhskt.theremin.redux.UiState

data class MainUiState(
    val frequency: Float,
    val volume: Float,
    val waveGraphicFrequency: Float,
    val backgroundGradientColors: List<Color>,
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
            backgroundGradientColors = listOf(
                ThereminColorPalette.lowVolume1,
                ThereminColorPalette.lowVolume1Light,
            ),
            note = "",
            pcConnected = false,
            watchConnected = false,
            appSoundEnabled = false,
            browserSoundEnabled = false,
        )
    }
}
