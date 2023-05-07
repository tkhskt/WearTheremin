package com.tkhskt.theremin.wear.theremin.ui.model

data class MainState(
    val started: Boolean,
) {
    companion object {
        val INITIAL = MainState(
            started = false,
        )
    }
}
