package com.tkhskt.theremin.ui.model

import com.tkhskt.theremin.redux.ViewState

data class MainViewState(
    val frequency: String,
    val volume: String,
) : ViewState {
    companion object {
        val Initial = MainViewState(
            frequency = "",
            volume = "",
        )
    }
}
