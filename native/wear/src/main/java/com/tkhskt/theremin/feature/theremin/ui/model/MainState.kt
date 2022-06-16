package com.tkhskt.theremin.feature.theremin.ui.model

import com.tkhskt.theremin.redux.State

data class MainState(
    val started: Boolean,
) : State {
    companion object {
        val INITIAL = MainState(
            started = false,
        )
    }
}
