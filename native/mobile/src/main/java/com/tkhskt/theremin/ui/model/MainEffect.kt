package com.tkhskt.theremin.ui.model

import com.tkhskt.theremin.redux.SideEffect

sealed interface MainEffect : SideEffect {
    object StartCamera : MainEffect
}
