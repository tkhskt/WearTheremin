package com.tkhskt.theremin.ui.model

data class MainState(
    val frequency: String,
    val volume: String,
) {
    companion object {
        val Empty = MainState(
            frequency = "",
            volume = "",
        )
    }
}
