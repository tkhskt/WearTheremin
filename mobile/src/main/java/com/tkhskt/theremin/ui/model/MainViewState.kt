package com.tkhskt.theremin.ui.model

import com.tkhskt.theremin.redux.ViewState

data class MainViewState(
    val frequency: Float,
    val volume: Float,
) : ViewState {
    companion object {
        val Initial = MainViewState(
            frequency = 0f,
            volume = 0f,
        )
    }
}
