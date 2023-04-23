package com.tkhskt.theremin.wear.theremin.ui.model

import com.tkhskt.theremin.redux.SideEffect

sealed interface MainEffect : SideEffect {
    object StartSensor : MainEffect
}
