package com.tkhskt.theremin.feature.theremin.ui.reducer

import com.tkhskt.theremin.redux.Reducer
import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainState

class MainReducer : Reducer<MainAction, MainState> {

    override suspend fun reduce(action: MainAction, state: MainState): MainState {
        return when (action) {
            is MainAction.ClickStartButton -> {
                state.copy(
                    started = true,
                )
            }
            is MainAction.ClickStopButton -> {
                state.copy(
                    started = false,
                )
            }
            else -> {
                state
            }
        }
    }
}
