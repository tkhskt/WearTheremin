package com.tkhskt.theremin.ui.model

data class MainState(
    val gravity: String,
) {
    companion object {
        val Empty = MainState(
            gravity = "",
        )
    }
}
