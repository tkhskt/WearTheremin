package com.tkhskt.theremin.feature.theremin.ui.reducer

import com.tkhskt.theremin.redux.Reducer
import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainState

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
            is MainAction.ClickAppSoundButton -> {
                state.copy(appSoundEnabled = !state.appSoundEnabled)
            }
            is MainAction.ClickBrowserSoundButton -> {
                state.copy(browserSoundEnabled = !state.browserSoundEnabled)
            }
            else -> {
                state
            }
        }
    }
}
