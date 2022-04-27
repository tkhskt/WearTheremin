package com.tkhskt.theremin.ui.model

data class MainState(
    val acceleration: String,
) {
    companion object {
        val Empty = MainState(
            acceleration = "",
        )
    }
}
