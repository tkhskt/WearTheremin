package com.tkhskt.theremin.feature.theremin.ui.reducer

import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminState
import com.tkhskt.theremin.redux.Reducer

class ThereminReducer : Reducer<ThereminAction, ThereminState> {

    override suspend fun reduce(action: ThereminAction, state: ThereminState): ThereminState {
        return when (action) {
            is ThereminAction.InitializeBle -> {
                state.copy(bluetoothInitialized = true)
            }
            is ThereminAction.FrequencyChanged -> {
                state.copy(frequency = action.frequency)
            }
            is ThereminAction.VolumeChanged -> {
                state.copy(volume = action.volume)
            }
            is ThereminAction.ClickAppSoundButton -> {
                state.copy(appSoundEnabled = !state.appSoundEnabled)
            }
            is ThereminAction.ClickBrowserSoundButton -> {
                state.copy(browserSoundEnabled = !state.browserSoundEnabled)
            }
            else -> {
                state
            }
        }
    }
}
