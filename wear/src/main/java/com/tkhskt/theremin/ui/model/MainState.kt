package com.tkhskt.theremin.ui.model

data class MainState(
    val started: Boolean,
) {
    companion object {
        val Empty = MainState(
            started = false,
        )
    }
}
