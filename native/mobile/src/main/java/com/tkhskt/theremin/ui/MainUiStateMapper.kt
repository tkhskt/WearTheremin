package com.tkhskt.theremin.ui

import androidx.compose.ui.graphics.Color
import com.tkhskt.theremin.core.NoteMapper
import com.tkhskt.theremin.ui.model.MainState
import com.tkhskt.theremin.ui.model.MainUiState
import com.tkhskt.theremin.ui.theme.ThereminColorPalette

object MainUiStateMapper {
    fun mapFromState(state: MainState): MainUiState {
        return MainUiState(
            frequency = state.frequency,
            volume = state.volume,
            waveGraphicFrequency = state.frequency / 60f,
            backgroundGradientColors = getGradientColorsByVolume(state.volume),
            note = NoteMapper.mapFromFrequency(state.frequency).note,
            pcConnected = state.pcConnected,
            watchConnected = state.watchConnected,
            appSoundEnabled = state.appSoundEnabled,
            browserSoundEnabled = state.browserSoundEnabled,
        )
    }

    private fun getGradientColorsByVolume(volume: Float): List<Color> {
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
