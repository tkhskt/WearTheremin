package com.tkhskt.theremin.ui.model

import com.tkhskt.theremin.redux.UiState

data class MainUiState(
    val frequency: Float,
    val volume: Float,
) : UiState {
    companion object {
        val Initial = MainUiState(
            frequency = 0f,
            volume = 0f,
        )
    }
}
