package com.tkhskt.theremin.feature.theremin.ui.model

import androidx.compose.ui.graphics.Color
import com.tkhskt.theremin.core.ui.ThereminColorPalette

data class ThereminUiState(
    val frequency: Float,
    val volume: Float,
    val waveGraphicFrequency: Float,
    val note: String,
    val watchConnected: Boolean,
    val appSoundEnabled: Boolean,
    val browserSoundEnabled: Boolean,
    val showMeteor: Boolean,
) {
    companion object {
        val Initial = ThereminUiState(
            frequency = 0f,
            volume = 0f,
            waveGraphicFrequency = 0f,
            note = "",
            watchConnected = false,
            appSoundEnabled = false,
            browserSoundEnabled = false,
            showMeteor = false,
        )
    }

    val backgroundGradientColors: List<Color>
        get() {
            val interval = 0.2
            return if (volume <= interval * 1) {
                listOf(ThereminColorPalette.lowVolume1Light, ThereminColorPalette.lowVolume1)
            } else if (volume <= interval * 2) {
                listOf(ThereminColorPalette.lowVolume2Light, ThereminColorPalette.lowVolume2)
            } else if (volume <= interval * 3) {
                listOf(ThereminColorPalette.midVolumeLight, ThereminColorPalette.midVolume)
            } else if (volume <= interval * 4) {
                listOf(ThereminColorPalette.highVolume1Light, ThereminColorPalette.highVolume1)
            } else {
                listOf(ThereminColorPalette.highVolume2Light, ThereminColorPalette.highVolume2)
            }
        }
}
