package com.tkhskt.theremin.feature.theremin.ui.model

import com.tkhskt.theremin.core.NoteMapper

data class ThereminState(
    val frequency: Float,
    val volume: Float,
    val cameraStarted: Boolean,
    val bluetoothInitialized: Boolean,
    val pcConnected: Boolean,
    val watchConnected: Boolean,
    val appSoundEnabled: Boolean,
    val browserSoundEnabled: Boolean,
    val shaked: Boolean,
) {
    companion object {
        val INITIAL = ThereminState(
            frequency = 0f,
            volume = 0f,
            cameraStarted = false,
            bluetoothInitialized = false,
            pcConnected = false,
            watchConnected = false,
            appSoundEnabled = false,
            browserSoundEnabled = false,
            shaked = false,
        )
    }

    val uiState = ThereminUiState(
        frequency = frequency,
        volume = volume,
        waveGraphicFrequency = frequency / 60f,
        note = NoteMapper.mapFromFrequency(frequency).note,
        pcConnected = pcConnected,
        watchConnected = watchConnected,
        appSoundEnabled = appSoundEnabled,
        browserSoundEnabled = browserSoundEnabled,
        showMeteor = shaked,
    )
}
