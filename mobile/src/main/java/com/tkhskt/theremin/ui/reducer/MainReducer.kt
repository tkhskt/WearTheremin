package com.tkhskt.theremin.ui.reducer

import com.tkhskt.theremin.ui.model.MainState
import com.tkhskt.theremin.redux.Reducer
import com.tkhskt.theremin.ui.model.MainAction

class MainReducer : Reducer<MainAction, MainState> {

    override suspend fun reduce(action: MainAction, state: MainState): MainState {
        return when (action) {
            is MainAction.InitializeBle -> {
                state.copy(bluetoothInitialized = true)
            }
            is MainAction.FrequencyChanged -> {
                state.copy(frequency = action.frequency)
            }
            is MainAction.VolumeChanged -> {
                state.copy(volume = action.volume)
            }
            else -> {
                state
            }
        }
    }
}
