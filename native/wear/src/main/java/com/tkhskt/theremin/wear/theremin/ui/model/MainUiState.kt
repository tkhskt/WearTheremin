package com.tkhskt.theremin.wear.theremin.ui.model

import com.tkhskt.theremin.redux.UiState

data class MainUiState(
    val started: Boolean,
) : UiState {
    companion object {
        val Initial = MainUiState(
            started = false,
        )
    }
}
