package com.tkhskt.theremin.feature.theremin.ui.model

import com.tkhskt.theremin.core.NoteMapper

data class ThereminState(
    val frequency: Float,
    val volume: Float,
    val cameraStarted: Boolean,
    val watchConnected: Boolean,
    val appSoundEnabled: Boolean,
    val shook: Boolean,
) {
    companion object {
        val INITIAL = ThereminState(
            frequency = 0f,
            volume = 0f,
            cameraStarted = false,
            watchConnected = false,
            appSoundEnabled = false,
            shook = false,
        )
    }

    val uiState = ThereminUiState(
        frequency = frequency,
        volume = volume,
        waveGraphicFrequency = frequency / 60f,
        note = NoteMapper.mapFromFrequency(frequency).note,
        watchConnected = watchConnected,
        appSoundEnabled = appSoundEnabled,
        showMeteor = shook,
    )
}
