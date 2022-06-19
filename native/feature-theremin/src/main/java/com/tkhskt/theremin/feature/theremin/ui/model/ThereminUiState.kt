package com.tkhskt.theremin.feature.theremin.ui.model

import androidx.compose.ui.graphics.Color
import com.tkhskt.theremin.core.ui.ThereminColorPalette
import com.tkhskt.theremin.redux.UiState

data class ThereminUiState(
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
        val Initial = ThereminUiState(
            frequency = 0f,
            volume = 0f,
            waveGraphicFrequency = 0f,
            backgroundGradientColors = listOf(
                ThereminColorPalette.lowVolume1Light,
                ThereminColorPalette.lowVolume1,
            ),
            note = "",
            pcConnected = false,
            watchConnected = false,
            appSoundEnabled = false,
            browserSoundEnabled = false,
        )
    }
}
