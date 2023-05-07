package com.tkhskt.theremin.wear.theremin.ui.model

data class MainUiState(
    val started: Boolean,
) {
    companion object {
        val Initial = MainUiState(
            started = false,
        )
    }
}
