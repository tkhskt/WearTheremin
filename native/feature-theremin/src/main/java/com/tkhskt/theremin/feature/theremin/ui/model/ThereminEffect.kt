package com.tkhskt.theremin.feature.theremin.ui.model

import com.tkhskt.theremin.redux.SideEffect

sealed interface ThereminEffect : SideEffect {
    object StartCamera : ThereminEffect
    object NavigateToLicense : ThereminEffect
}
