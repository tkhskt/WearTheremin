package com.tkhskt.theremin.ui

import com.tkhskt.theremin.core.NoteMapper
import com.tkhskt.theremin.ui.model.MainState
import com.tkhskt.theremin.ui.model.MainUiState

object MainUiStateMapper {
    fun mapFromState(state: MainState): MainUiState {
        return MainUiState(
            frequency = state.frequency,
            volume = state.volume,
            waveGraphicFrequency = state.frequency / 60f,
            note = NoteMapper.mapFromFrequency(state.frequency).note,
            pcConnected = state.pcConnected,
            watchConnected = state.watchConnected,
            appSoundEnabled = state.appSoundEnabled,
            browserSoundEnabled = state.browserSoundEnabled,
        )
    }
}
